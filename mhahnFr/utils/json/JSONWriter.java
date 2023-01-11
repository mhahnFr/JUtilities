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

import mhahnFr.utils.Pair;
import mhahnFr.utils.gui.abstraction.FStyle;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
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
        final var list           = new HashSet<>(Arrays.asList(declaredFields));

        for (final var field : declaredFields) {
            field.setAccessible(true);
        }

        list.addAll(Arrays.asList(publicFields));

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

    private void writePrimitive(final Field field, final Object obj) throws IOException {
        writeFieldName(field.getName());

        if (humanReadable) { write(" "); }

        write(obj.toString());
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
            final var it = getFields(obj).iterator();
            while (it.hasNext()) {
                final var field = it.next();
                if (field.getType().isPrimitive()) {
                    writePrimitive(field, obj);
                } else {
                    writeObject(field, obj);
                }
                if (it.hasNext()) {
                    write(",");
                    if (humanReadable) { write("\n"); }
                }
            }
            if (humanReadable) { write("\n"); }
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
