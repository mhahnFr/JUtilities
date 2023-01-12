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
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

    private Collection<Field> getFields(final Object obj) {
        final var declaredFields = obj.getClass().getDeclaredFields();
        final var publicFields   = obj.getClass().getFields();
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

    private void write(final String string) throws IOException {
        out.write(string.getBytes(charset));
    }

    private void writeIndent(final String string) throws IOException {
        write(" ".repeat(Math.max(0, indent)) + string);
    }

    private void writeFieldName(final String name) throws IOException {
        writeIndent("\"" + name + "\":");
    }

    private boolean canDumpDirect(final Field field, final Object obj) throws IllegalAccessException {
        final var content = field.get(obj);
        return canDumpDirect(content);
    }

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

    private void writePrimitive(final Field field, final Object obj) throws IOException, IllegalAccessException {
        writeFieldName(field.getName());

        if (humanReadable) { write(" "); }

        final var content = field.get(obj);
        writePrimitive(content);
    }

    private void writePrimitive(final Object obj) throws IOException {
        final var needsQuotation = obj instanceof String || obj instanceof Enum;

        if (needsQuotation) { write("\""); }
        write(obj.toString());
        if (needsQuotation) { write("\""); }
    }

    private void dumpDictionary(final Map<?, ?> dict) throws IOException, IllegalAccessException {
        final var it = dict.entrySet().iterator();
        while (it.hasNext()) {
            final var entry = it.next();

            final var key = entry.getKey();

            writeIndent("");
            if (canDumpDirect(key)) {
                writePrimitive(key);
            } else {
                writeObject(key);
            }

            write(",");
            if (humanReadable) { write("\n"); }

            final var value = entry.getValue();

            writeIndent("");
            if (canDumpDirect(value)) {
                writePrimitive(value);
            } else {
                writeObject(value);
            }

            if (it.hasNext()) {
                write(",");
                if (humanReadable) { write("\n"); }
            }
        }
    }

    private void dumpList(final Collection<?> list) throws IOException, IllegalAccessException {
        final var it = list.iterator();
        while (it.hasNext()) {
            final var element = it.next();

            writeIndent("");
            if (canDumpDirect(element)) {
                writePrimitive(element);
            } else {
                writeObject(element);
            }

            if (it.hasNext()) {
                write(",");
                if (humanReadable) { write("\n"); }
            }
        }
    }

    private void dumpArray(final Object array) throws IOException, IllegalAccessException {
        final var length = Array.getLength(array);

        for (int i = 0; i < length; ++i) {
            final var element = Array.get(array, i);

            writeIndent("");
            if (canDumpDirect(element)) {
                writePrimitive(element);
            } else {
                writeObject(element);
            }

            if (i + 1 < length) {
                write(",");
                if (humanReadable) {
                    write("\n");
                }
            }
        }
    }

    private void writeObject(final Field field, final Object obj) throws IllegalAccessException, IOException {
        writeFieldName(field.getName());

        if (humanReadable) { write(" "); }

        final var content = field.get(obj);
        writeObject(content);
    }

    private void writeObject(final Object obj) throws IOException, IllegalAccessException {
        if (obj == null) {
            write("{}");
            return;
        }

        final var c = obj.getClass();

        final var isCollection = Collection.class.isAssignableFrom(c);
        final var isDictionary = Map.class.isAssignableFrom(c);
        final var isArray      = c.isArray();

        if (isCollection || isDictionary || isArray) {
            write("[");

            if (humanReadable) {
                write("\n");
                indent += 4;
            }

            if (isDictionary) {
                dumpDictionary((Map<?, ?>) obj);
            } else if (isCollection) {
                dumpList((Collection<?>) obj);
            } else {
                dumpArray(obj);
            }
        } else {
            dump(obj);
        }
        if (isCollection || isDictionary || isArray) {
            if (humanReadable) {
                write("\n");
                indent -= 4;
            }
            writeIndent("]");
        }
    }

    public void dump(Object obj) throws IllegalAccessException, IOException {
        write("{");
        if (humanReadable) {
            write("\n");
            indent += 4;
        }
        if (obj != null) {
            final var fields = getFields(obj);
            final var it     = fields.iterator();
            while (it.hasNext()) {
                final var field = it.next();
                if (canDumpDirect(field, obj)) {
                    writePrimitive(field, obj);
                } else {
                    writeObject(field, obj);
                }
                if (it.hasNext()) {
                    write(",");
                    if (humanReadable) { write("\n"); }
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
