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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * This class parses JSON data.
 *
 * @author mhahnFr
 * @since 13.01.23
 */
public class JSONParser {
    private final StringStream stream;

    public JSONParser(StringStream stream) {
        this.stream = stream;
    }

    private void skipWhitespaces() {
        while (stream.hasNext() && Character.isWhitespace(stream.peek())) {
            stream.skip();
        }
    }

    private void expect(final String string) {
        expectKeep(string);
        stream.skip(string.length());
    }

    private void expectKeep(final String string) {
        if (!stream.peek(string)) {
            throw new RuntimeException("Expected \"" + string + "\"!");
        }
    }

    private String readString() {
        expect("\"");

        final var buffer = new StringBuilder();
        while (stream.hasNext() && !stream.peek('"')) {
            buffer.append(stream.next());
        }
        expect("\"");
        return buffer.toString();
    }

    private String readField() {
        final var toReturn = readString();
        skipWhitespaces();
        expect(":");
        return toReturn;
    }

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

    private boolean peekConsume(final String string) {
        if (stream.peek(string)) {
            stream.skip(string.length());
            return true;
        }
        return false;
    }

    private void readFields(Object obj) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        skipWhitespaces();
        if (stream.peek('}')) return;

        do {
            readField(obj);
        } while (peekConsume(","));
    }

    private Object readObject(final Class<?> c) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        skipWhitespaces();
        if (stream.peek('{')) {
            // object
            final var value = c.getConstructor().newInstance();
            readInto(value);
            return value;
        } else if (peekConsume("[")) {
            // collection
            if (c.isArray()) {
                final var underlying = c.componentType();
                skipWhitespaces();
                if (peekConsume("]")) {
                    return Array.newInstance(underlying, 0);
                }
                final var list = new ArrayList<>();
                do {
                    list.add(readObject(underlying));
                } while (peekConsume(","));
                skipWhitespaces();
                expect("]");

                final var toReturn = Array.newInstance(underlying, list.size());
                for (int i = 0; i < list.size(); ++i) {
                    Array.set(toReturn, i, list.get(i));
                }
                return toReturn;
            } else if (Collection.class.isAssignableFrom(c)) {
                /*final Collection collection;
                if (c.isInterface()) {
                    collection = new ArrayList<>();
                } else {
                    collection = (Collection) c.getConstructor().newInstance();
                }

                stream.skip();
                skipWhitespaces();

                if (stream.peek(']')) {

                } else {
                    final var underlying = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    do {
                        collection.add(readObject(underlying));
                    } while (!peekConsume(","));
                    expect("]");
                }

//                final var type = (ParameterizedType) field.getGenericType();
//                final var a = (Class<?>) type.getActualTypeArguments()[0];
//                System.out.println(a);

                return collection;*/
            } else if (Map.class.isAssignableFrom(c)) {

            } else {
                // Problem!
            }
        } else if (stream.peek('"')) {
            // string or enum
            final var buffer = readString();
            if (Enum.class.isAssignableFrom(c)) {
                return c.getMethod("valueOf", String.class).invoke(null, buffer);
            } else {
                return buffer;
            }
        } else {
            // raw value
            final var buffer = new StringBuilder();
            while (stream.hasNext() && !(Character.isWhitespace(stream.peek()) || stream.peek(',') || stream.peek('}') || stream.peek(']'))) {
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
            } else {
                return Double.valueOf(string);
            }
        }
        return null;
    }

    private void readField(Object obj) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        skipWhitespaces();

        final var field = getField(obj, readField());

        field.set(obj, readObject(field.getType()));
    }

    public void readInto(Object obj) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        skipWhitespaces();
        expect("{");
        readFields(obj);
        skipWhitespaces();
        expect("}");
    }
}
