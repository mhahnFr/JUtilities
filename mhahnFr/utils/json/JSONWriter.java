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
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
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
    private final OutputStream out;
    private Charset charset = StandardCharsets.UTF_8;
    private boolean humanReadable = false;
    private int indent = 0;

    public JSONWriter(OutputStream out) {
        this.out = out;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    private Collection<Field> getFields(final Object obj) {
        final var declaredFields = obj.getClass().getDeclaredFields();
        final var publicFields   = obj.getClass().getFields();
        final var list           = new HashSet<>(Arrays.asList(publicFields));

        for (final var field : declaredFields) {
            try {
                field.setAccessible(true);
                list.add(field);
            } catch (InaccessibleObjectException ignored) {}
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
        final var c = field.getType();
        return field.get(obj) != null && (
                c.isPrimitive()           ||
                c.equals(Boolean.class)   ||
                c.equals(Byte.class)      ||
                c.equals(Short.class)     ||
                c.equals(Integer.class)   ||
                c.equals(Long.class)      ||
                c.equals(Float.class)     ||
                c.equals(Double.class)    ||
                c.equals(Character.class) ||
                c.equals(String.class)
            );
    }

    private void writePrimitive(final Field field, final Object obj) throws IOException, IllegalAccessException {
        writeFieldName(field.getName());

        if (humanReadable) { write(" "); }

        if (obj instanceof String) { write("\""); }
        write(field.get(obj).toString());
        if (obj instanceof String) { write("\""); }
    }

    private void writeObject(final Field field, final Object obj) throws IllegalAccessException, IOException {
        writeFieldName(field.getName());
        final var isCollDict = Collection.class.isAssignableFrom(field.getType());

        if (humanReadable) { write(" "); }

        if (isCollDict) {
            write("[");
        }
        dump(field.get(obj));
        if (isCollDict) {
            if (humanReadable) {
                write("\n");
                indent -= 4;
            }
            write("]");
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

    public boolean isHumanReadable() {
        return humanReadable;
    }

    public void setHumanReadable(boolean humanReadable) {
        this.humanReadable = humanReadable;
    }
}
