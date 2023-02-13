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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class writes objects into a JSON-format.
 *
 * @author mhahnFr
 * @since 11.01.23
 */
public class JSONWriter {
    /** The output stream to write the JSON data to.        */
    private final OutputStream out;
    /** The charset to be used, defaults to UTF-8.          */
    private Charset charset = StandardCharsets.UTF_8;
    /** Indicates whether to write in a human-readable way. */
    private boolean humanReadable = false;
    /** The current indentation level.                      */
    private int indent = 0;

    /**
     * Constructs this writer using the given output stream.
     *
     * @param out the output stream to be used
     * @throws NullPointerException in case no output stream is given
     */
    public JSONWriter(OutputStream out) {
        if (out == null) throw new NullPointerException("The output stream must not be null!");

        this.out = out;
    }

    /**
     * Returns the currently used charset.
     *
     * @return the charset used for writing
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Sets the charset used for writing.
     *
     * @param charset the charset to be used for the writing
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * Returns a collection of all accessible and non-static fields
     * of the class of the given object.
     *
     * @param obj the object whose fields to get
     * @return a collection with all accessible fields
     */
    private Collection<Field> getFields(final Object obj) {
        final var c              = obj.getClass();
        final var declaredFields = c.getDeclaredFields();
        final var publicFields   = c.getFields();
        final var list           = new HashSet<Field>();

        for (final var field : publicFields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                list.add(field);
            }
        }

        for (final var field : declaredFields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                if (field.trySetAccessible()) {
                    list.add(field);
                }
            }
        }

        return list;
    }

    /**
     * Writes the given string using the set charset.
     *
     * @param string the string to be written
     * @throws IOException if an I/O error occurs
     * @see #setCharset(Charset)
     * @see #getCharset()
     * @see #charset
     */
    private void write(final String string) throws IOException {
        out.write(string.getBytes(charset));
    }

    /**
     * Writes a comma. If the output should be human-readable,
     * a newline is written after the comma.
     *
     * @throws IOException if an I/O error occurs
     * @see #write(String)
     * @see #humanReadable
     * @see #isHumanReadable()
     * @see #setHumanReadable(boolean)
     */
    private void writeComma() throws IOException {
        write(",");

        if (humanReadable) { write("\n"); }
    }

    /**
     * Writes a colon. If the output should be human-readable,
     * a space is written after the comma.
     *
     * @throws IOException if an I/O error occurs
     */
    private void writeColon() throws IOException {
        write(":");

        if (humanReadable) { write(" "); }
    }

    /**
     * Writes the beginning bracket.
     *
     * @param bracket the actual bracket to be printed
     * @throws IOException if an I/O error occurs
     * @see #write(String)
     */
    private void writeBeginBracket(final char bracket) throws IOException {
        write(Character.toString(bracket));

        if (humanReadable) {
            write("\n");
            indent += 4;
        }
    }

    /**
     * Writes the end bracket.
     *
     * @param bracket the actual bracket to be printed
     * @throws IOException if an I/O error occurs
     * @see #write(String)
     * @see #writeIndent(String)
     */
    private void writeEndBracket(final char bracket) throws IOException {
        if (humanReadable) {
            write("\n");
            indent -= 4;
        }
        writeIndent(Character.toString(bracket));
    }

    /**
     * Writes the given string indented.
     *
     * @param string the string to write
     * @throws IOException if an I/O error occurs
     * @see #write(String)
     * @see #indent
     */
    private void writeIndent(final String string) throws IOException {
        write(" ".repeat(Math.max(0, indent)) + string);
    }

    /**
     * Writes the name of a field. If the output should be
     * human-readable, a space is written after it.
     *
     * @param name the name of the field to be written
     * @throws IOException if an I/O error occurs
     * @see #writeIndent(String)
     * @see #humanReadable
     * @see #isHumanReadable()
     * @see #setHumanReadable(boolean)
     */
    private void writeFieldName(final String name) throws IOException {
        writeIndent("\"" + name + "\"");
        writeColon();
    }

    /**
     * Returns whether the given object can be written using
     * its {@link Object#toString() toString()} method.
     *
     * @param obj the object to be checked
     * @return whether the given object can be written as a string
     */
    private boolean canDumpDirect(final Object obj) {
        final var c = obj == null ? null : obj.getClass();

        return obj != null && (
                c.isPrimitive()           ||
                c.equals(Boolean.class)   ||
                c.equals(Byte.class)      ||
                c.equals(Short.class)     ||
                c.equals(Integer.class)   ||
                c.equals(Long.class)      ||
                c.equals(Float.class)     ||
                c.equals(Double.class)    ||
                c.equals(Character.class) ||
                c.equals(String.class)    ||
                Enum.class.isAssignableFrom(c)
            );
    }

    /**
     * Writes a primitive object. That is, the object is dumped using
     * its {@link Object#toString() toString()} method. The object is
     * quoted if necessary.
     *
     * @param obj the object to be dumped
     * @throws IOException if an I/O error occurs
     * @see #write(String)
     */
    private void writePrimitive(final Object obj) throws IOException {
        final var needsQuotation = obj instanceof String || obj instanceof Enum;

        if (needsQuotation) { write("\""); }
        write(obj.toString());
        if (needsQuotation) { write("\""); }
    }

    /**
     * Dumps the given dictionary.
     *
     * @param dict the dictionary to be dumped
     * @param type the generic type
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if a field of a dumped object cannot be accessed
     * @see #dumpArrayElement(Object, Type, boolean)
     * @see #writeComma()
     */
    private void dumpDictionary(final Map<?, ?> dict, final Type type) throws IOException, IllegalAccessException {
        final var keyClass = JSONParser.getMaybeGenericClass(((ParameterizedType) type).getActualTypeArguments()[0]);
        final var isStringDict = String.class.isAssignableFrom(keyClass) ||
                                 Enum.class.isAssignableFrom(keyClass);

        writeBeginBracket(isStringDict ? '{' : '[');

        final var it = dict.entrySet().iterator();
        while (it.hasNext()) {
            final var entry = it.next();

            dumpArrayElement(entry.getKey(), type, true);
            if (isStringDict) {
                writeColon();
            } else {
                writeComma();
            }
            dumpArrayElement(entry.getValue(), type, !isStringDict);

            if (it.hasNext()) { writeComma(); }
        }

        writeEndBracket(isStringDict ? '}' : ']');
    }

    /**
     * Dumps the given list.
     *
     * @param list the list to be dumped
     * @param type the generic type
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if a field of a dumped object cannot be accessed
     * @see #dumpArrayElement(Object, Type, boolean)
     * @see #writeComma()
     */
    private void dumpList(final Collection<?> list, final Type type) throws IOException, IllegalAccessException {
        writeBeginBracket('[');

        final var it = list.iterator();
        while (it.hasNext()) {
            dumpArrayElement(it.next(), type, true);

            if (it.hasNext()) { writeComma(); }
        }

        writeEndBracket(']');
    }

    /**
     * Dumps the given array.
     *
     * @param array the array to be dumped
     * @param type the generic type
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if a field of a dumped object cannot be accessed
     * @see #dumpArrayElement(Object, Type, boolean)
     * @see #writeComma()
     */
    private void dumpArray(final Object array, final Type type) throws IOException, IllegalAccessException {
        writeBeginBracket('[');

        final var length = Array.getLength(array);

        for (int i = 0; i < length; ++i) {
            dumpArrayElement(Array.get(array, i), type, true);

            if (i + 1 < length) { writeComma(); }
        }

        writeEndBracket(']');
    }

    /**
     * Dumps the given object as a collection element. If it can
     * be dumped direct, it is written as a primitive, otherwise
     * it is written as an object.
     *
     * @param obj the object to be written
     * @param type the generic type
     * @param indent whether to indent the element
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if a field of a dumped object cannot be accessed
     * @see #canDumpDirect(Object)
     * @see #writePrimitive(Object)
     * @see #writeObject(Object, Type)
     * @see #writeIndent(String)
     */
    private void dumpArrayElement(final Object obj, final Type type, final boolean indent) throws IOException, IllegalAccessException {
        if (indent) { writeIndent(""); }

        if (canDumpDirect(obj)) {
            writePrimitive(obj);
        } else {
            writeObject(obj, type);
        }
    }

    /**
     * Writes the given object. If the given object is a collection
     * type, it is written as a collection, otherwise it is dumped
     * using {@link #dump(Object)}.
     * The output is formatted human-readable if set.
     *
     * @param obj the object to be written
     * @param type the generic type
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if a field of a dumped object cannot be accessed
     * @see #write(String)
     * @see #writeIndent(String)
     * @see #indent
     * @see #dump(Object)
     * @see #dumpArray(Object, Type)
     * @see #dumpDictionary(Map, Type)
     * @see #dumpList(Collection, Type)
     * @see #humanReadable
     * @see #isHumanReadable()
     * @see #setHumanReadable(boolean)
     */
    private void writeObject(final Object obj, final Type type) throws IOException, IllegalAccessException {
        final var c = obj.getClass();

        final var isCollection = Collection.class.isAssignableFrom(c);
        final var isDictionary = Map.class.isAssignableFrom(c);
        final var isArray      = c.isArray();

        if (isCollection || isDictionary || isArray) {
            if (isDictionary) {
                dumpDictionary((Map<?, ?>) obj, type);
            } else if (isCollection) {
                dumpList((Collection<?>) obj, type);
            } else {
                dumpArray(obj, type);
            }
        } else {
            dump(obj);
        }
    }

    /**
     * Writes the given object to the set output stream using the JSON
     * format.
     *
     * @param obj the object to be dumped
     * @throws IllegalAccessException if a field of the object cannot be accessed
     * @throws IOException if an I/O error occurs
     */
    public void dump(Object obj) throws IllegalAccessException, IOException {
        writeBeginBracket('{');
        if (obj != null) {
            final var fields = getFields(obj);
            final var it     = fields.iterator();

            boolean needsComma = false;
            while (it.hasNext()) {
                final var field = it.next();

                if (field.isAnnotationPresent(JSONNoSerialization.class)) continue;

                final var content = field.get(obj);

                if (content != null) {
                    if (needsComma) { writeComma(); }

                    writeFieldName(field.getName());
                    if (canDumpDirect(content)) {
                        writePrimitive(content);
                    } else {
                        writeObject(content, field.getGenericType());
                    }
                    if (it.hasNext()) { needsComma = true; }
                }
            }
            if (humanReadable && !fields.isEmpty()) { write("\n"); }
        }
        if (humanReadable) { indent -= 4; }
        writeIndent("}");
    }

    /**
     * Returns whether the output will be formatted in a
     * human-readable way.
     *
     * @return whether the output is human-readable
     */
    public boolean isHumanReadable() {
        return humanReadable;
    }

    /**
     * Sets whether the JSON data should be written in
     * a human-readable way.
     *
     * @param humanReadable whether to write human-readable
     */
    public void setHumanReadable(boolean humanReadable) {
        this.humanReadable = humanReadable;
    }
}
