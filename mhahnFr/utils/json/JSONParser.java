/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2023  mhahnFr
 *
 * This file is part of the JUtilities. This library is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this library, see the file LICENSE.  If not, see <https://www.gnu.org/licenses/>.
 */

package mhahnFr.utils.json;

import mhahnFr.utils.StringStream;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * This class parses JSON data.
 *
 * @author mhahnFr
 * @since 13.01.23
 */
public class JSONParser {
    /** The stream to read the data from. */
    private final StringStream stream;

    /**
     * Constructs this parser using the given {@link StringStream}.
     *
     * @param stream the stream to read the data from
     */
    public JSONParser(StringStream stream) {
        this.stream = stream;
    }

    /**
     * Skips the whitespaces that follow.
     */
    private void skipWhitespaces() {
        while (stream.hasNext() && Character.isWhitespace(stream.peek())) {
            stream.skip();
        }
    }

    /**
     * Checks whether the next characters equal the given string.
     * If that is the case, they are consumed, otherwise an exception
     * is thrown.
     *
     * @param string the string expected to follow
     * @throws JSONParseException if the JSON data could not be parsed
     */
    private void expect(final String string) throws JSONParseException {
        expectKeep(string);
        stream.skip(string.length());
    }

    /**
     * Checks whether the next characters equal the given string.
     * If this is not the case, an exception is thrown.
     *
     * @param string the string expected to follow
     * @throws JSONParseException if the JSON data could not be parsed
     */
    private void expectKeep(final String string) throws JSONParseException {
        if (!stream.peek(string)) {
            throw new JSONParseException("Expected \"" + string + "\"!", stream.getStreamPosition());
        }
    }

    /**
     * Reads a string. It is expected to be in the format: {@code "< content >"}.
     *
     * @return the read string, without quotation
     * @throws JSONParseException if the JSON data could not be parsed
     * @see #expect(String)
     */
    private String readString() throws JSONParseException {
        expect("\"");

        final var buffer = new StringBuilder();
        while (stream.hasNext() && !stream.peek('"')) {
            buffer.append(stream.next());
        }
        expect("\"");
        return buffer.toString();
    }

    /**
     * Reads a field. It is expected to be a string followed by a colon.
     *
     * @return the name of the read field
     * @throws JSONParseException if the JSON data could not be parsed
     * @see #expect(String)
     * @see #readString()
     */
    private String readField() throws JSONParseException {
        final var toReturn = readString();
        skipWhitespaces();
        expect(":");
        return toReturn;
    }

    /**
     * Returns the field named by the given string in the given object.
     *
     * @param obj the object whose field should be returned
     * @param name the name of the field
     * @return the field of the given object named by the given string
     * @throws NoSuchFieldException if the requested field does not exist in the given object
     */
    private Field getField(final Object obj, final String name) throws NoSuchFieldException {
        final var c = obj.getClass();

        try {
            return c.getField(name);
        } catch (NoSuchFieldException __) {
            final var field = c.getDeclaredField(name);
            field.trySetAccessible();
            return field;
        }
    }

    /**
     * Peeks the given string in the underlying string. If the given
     * string is following in the stream, it is consumed.
     *
     * @param string the string to be peeked
     * @return whether stream was following in the stream
     */
    private boolean peekConsume(final String string) {
        if (stream.peek(string)) {
            stream.skip(string.length());
            return true;
        }
        return false;
    }

    /**
     * Tries to decode a raw value from the stream.
     *
     * @param c the class of the object to be read
     * @return an object representing the read raw value
     */
    private Object readRawValue(Class<?> c) {
        final var buffer = new StringBuilder();
        while (stream.hasNext() && !(Character.isWhitespace(stream.peek()) ||
                                     stream.peek(',')                   ||
                                     stream.peek('}')                   ||
                                     stream.peek(']'))) {
            buffer.append(stream.next());
        }
        final var string = buffer.toString();

        final var isTrue = string.equals("true");
        final var isFalse = string.equals("false");
        if (isTrue || isFalse) {
            return isTrue;
        } else if (c.equals(Byte.class) || c.equals(Byte.TYPE)) {
            return Byte.decode(string);
        } else if (c.equals(Short.class) || c.equals(Short.TYPE)) {
            return Short.decode(string);
        } else if (c.equals(Integer.class) || c.equals(Integer.TYPE)) {
            return Integer.decode(string);
        } else if (c.equals(Long.class) || c.equals(Long.TYPE)) {
            return Long.decode(string);
        } else if (c.equals(Float.class) || c.equals(Float.TYPE)) {
            return Float.valueOf(string);
        }
        return Double.valueOf(string);
    }

    /**
     * Reads a string from the stream. Returns an enum representation if
     * the given class is an enum.
     *
     * @param c the class of the object to be read
     * @return a string or an enum representation depending on the given class
     * @throws ReflectiveOperationException if the class is an enum and its {@code valueOf} method cannot be invoked
     * @throws JSONParseException if the JSON data could not be parsed
     */
    private Object readStringEnum(Class<?> c) throws ReflectiveOperationException, JSONParseException {
        final var buffer = readString();
        if (Enum.class.isAssignableFrom(c)) {
            return c.getMethod("valueOf", String.class).invoke(null, buffer);
        }
        return buffer;
    }

    /**
     * Reads an array from the stream.
     *
     * @param c the class of the array to be read
     * @param type the generic type that might be present
     * @return an array consisting of the objects read from the stream
     * @throws ReflectiveOperationException if an object cannot be filled with the values
     * @throws JSONParseException if the JSON data could not be parsed
     */
    private Object readArray(Class<?> c, Type type) throws ReflectiveOperationException, JSONParseException {
        final var underlying = c.componentType();

        skipWhitespaces();
        if (stream.peek(']')) { return Array.newInstance(underlying, 0); }

        final var list = new ArrayList<>();
        do {
            list.add(readObject(underlying, type));
        } while (peekConsume(","));

        final var toReturn = Array.newInstance(underlying, list.size());
        for (int i = 0; i < list.size(); ++i) {
            Array.set(toReturn, i, list.get(i));
        }
        return toReturn;
    }

    /**
     * Reads a collection from the stream.
     *
     * @param c the class of the collection
     * @param type the generic type of the collection
     * @return a collection consisting of the objects read from the stream
     * @throws ReflectiveOperationException if an object cannot be filled with the values
     * @throws JSONParseException if the JSON data could not be parsed
     */
    @SuppressWarnings("unchecked")
    private Object readCollection(Class<?> c, Type type) throws ReflectiveOperationException, JSONParseException {
        final Collection<Object> collection;
        if (c.isInterface()) {
            collection = new ArrayList<>();
        } else {
            collection = (Collection<Object>) c.getConstructor().newInstance();
        }

        skipWhitespaces();
        if (stream.peek(']')) { return collection; }

        final var actualType = ((ParameterizedType) type).getActualTypeArguments()[0];

        final Class<?> underlying = getMaybeGenericClass(actualType);
        do {
            collection.add(readObject(underlying, actualType));
            skipWhitespaces();
        } while (peekConsume(","));
        return collection;
    }

    /**
     * Reads a mapping from the stream.
     *
     * @param c the class of the map
     * @param type the generic type of the map
     * @param isStringDict whether the map is a string dictionary
     * @return a map consisting of the objects read from the stream
     * @throws ReflectiveOperationException if an object cannot be filled with the values
     * @throws JSONParseException if the JSON data could not be parsed
     */
    @SuppressWarnings("unchecked")
    private Object readMap(Class<?> c, Type type, final boolean isStringDict) throws ReflectiveOperationException, JSONParseException {
        final Map<Object, Object> map;
        if (c.isInterface()) {
            map = new HashMap<>();
        } else {
            map = (Map<Object, Object>) c.getConstructor().newInstance();
        }

        skipWhitespaces();
        if (stream.peek(isStringDict ? '}' : ']')) { return map; }

        final var actualTypes = ((ParameterizedType) type).getActualTypeArguments();

        final var keyType     = actualTypes[0];
        final var valueType   = actualTypes[1];

        final Class<?> keyClass   = getMaybeGenericClass(keyType),
                       valueClass = getMaybeGenericClass(valueType);
        do {
            final var key = readObject(keyClass, keyType);
            skipWhitespaces();
            expect(isStringDict ? ":" : ",");
            final var value = readObject(valueClass, valueType);
            map.put(key, value);
            skipWhitespaces();
        } while (peekConsume(","));
        return map;
    }

    /**
     * Reads an array, collection or mapping from the stream.
     *
     * @param c the class of the collection
     * @param type the generic type of the collection
     * @param isStringDict whether the collection is a string dictionary
     * @return a collection consisting of the read objects
     * @throws ReflectiveOperationException if an object could not be filled with the values
     * @throws JSONParseException if the JSON data could not be parsed
     * @see #readArray(Class, Type)
     * @see #readCollection(Class, Type)
     * @see #readMap(Class, Type, boolean)
     */
    private Object readCollectionKind(Class<?> c, Type type, final boolean isStringDict) throws ReflectiveOperationException, JSONParseException {
        final Object toReturn;

        if (isStringDict || Map.class.isAssignableFrom(c)) {
            toReturn = readMap(c, type, isStringDict);
        } else if (c.isArray()) {
            toReturn = readArray(c, type);
        } else if (Collection.class.isAssignableFrom(c)) {
            toReturn = readCollection(c, type);
        } else {
            // Problem!
            throw new RuntimeException("Unknown collection type!");
        }
        skipWhitespaces();
        expect(isStringDict ? "}" : "]");
        return toReturn;
    }

    /**
     * Reads an object from the stream.
     *
     * @param c the class of the object
     * @return the read object
     * @throws ReflectiveOperationException if the object could not be filled with the read values
     * @throws JSONParseException if the JSON data could not be parsed
     */
    private Object readObjectKind(final Class<?> c) throws ReflectiveOperationException, JSONParseException {
        final var value = c.getConstructor().newInstance();
        readInto(value);
        return value;
    }

    /**
     * Returns the {@link Class} represented by the given type.
     *
     * @param type the type whose underlying class to get
     * @return the underlying class
     */
    public static Class<?> getMaybeGenericClass(final Type type) {
        final Class<?> c;

        if (type instanceof ParameterizedType) {
            c = (Class<?>) ((ParameterizedType) type).getRawType();
        } else {
            c = (Class<?>) type;
        }
        return c;
    }

    /**
     * Returns whether the given {@link Class} represents a {@link String}
     * dictionary.
     *
     * @param c the class to be checked
     * @param type the generic type of the class's instance
     * @return whether the given class and type represent a string dictionary
     */
    private boolean isStringDictionary(final Class<?> c, final Type type) {
        if (Map.class.isAssignableFrom(c)) {
            final var keyClass = getMaybeGenericClass(((ParameterizedType) type).getActualTypeArguments()[0]);

            return String.class.isAssignableFrom(keyClass) ||
                   Enum.class.isAssignableFrom(keyClass);
        }
        return false;
    }

    /**
     * Reads an object from the stream. Depending on the following characters,
     * either a raw value, a collection or a normal object is read and returned.
     *
     * @param c the class of the object that should be read
     * @param type the generic type of the object
     * @return the read object
     * @throws ReflectiveOperationException if the object could not be filled with the read values
     * @throws JSONParseException if the JSON data could not be parsed
     * @see #readObjectKind(Class)
     * @see #readCollectionKind(Class, Type, boolean)
     * @see #readStringEnum(Class)
     * @see #readRawValue(Class)
     */
    private Object readObject(final Class<?> c, final Type type) throws ReflectiveOperationException, JSONParseException {
        skipWhitespaces();

        final var isStringDict = isStringDictionary(c, type);

        if (stream.peek('{') && !isStringDict) {
            return readObjectKind(c);
        } else if (peekConsume(isStringDict ? "{" : "[")) {
            return readCollectionKind(c, type, isStringDict);
        } else if (stream.peek('"')) {
            return readStringEnum(c);
        }
        return readRawValue(c);
    }

    /**
     * Reads a field from the stream and fills it for the given object.
     *
     * @param obj the object to fill the field in
     * @throws ReflectiveOperationException if an object could not be filled with the read values
     * @throws JSONParseException if the JSON data could not be parsed
     * @see #readObject(Class, Type)
     */
    private void readField(Object obj) throws ReflectiveOperationException, JSONParseException {
        skipWhitespaces();

        final var field = getField(obj, readField());

        field.set(obj, readObject(field.getType(), field.getGenericType()));
    }

    /**
     * Reads the fields for the given object.
     *
     * @param obj the object to be filled
     * @throws ReflectiveOperationException if an object could not be filled with the read values
     * @throws JSONParseException if the JSON data could not be parsed
     * @see #readField(Object)
     */
    private void readFields(Object obj) throws ReflectiveOperationException, JSONParseException {
        skipWhitespaces();
        if (stream.peek('}')) return;

        do {
            readField(obj);
            skipWhitespaces();
        } while (peekConsume(","));
    }

    /**
     * Reads the values of the given object from the stream.
     *
     * @param obj the object to be filled
     * @throws ReflectiveOperationException if an object could not be filled with the read values
     * @throws JSONParseException if the JSON data could not be parsed
     */
    public void readInto(Object obj) throws ReflectiveOperationException, JSONParseException {
        skipWhitespaces();
        expect("{");
        readFields(obj);
        skipWhitespaces();
        expect("}");
    }
}
