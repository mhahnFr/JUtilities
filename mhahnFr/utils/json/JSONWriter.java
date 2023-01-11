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

    private void writeFieldName(final String name) throws IOException {
        write("\"" + name + "\":");
    }

    private void writePrimitive(final Field field, final Object obj) throws IOException {
        // write "name": <primitive>
        writeFieldName(field.getName());
        write(obj.toString());
    }

    private void writeObject(final Field field, final Object obj) throws IllegalAccessException, IOException {
        // Write "name":
        // if is collection / dictionary: write [, otherwise {
        writeFieldName(field.getName());
        final var isCollDict = Collection.class.isAssignableFrom(field.getType());
        write(isCollDict ? "[" : "{");
        dump(field.get(obj));
        write(isCollDict ? "]" : "}");
        // if was collection / dictionary: write ] or }
    }

    public void dump(Object obj) throws IllegalAccessException, IOException {
        // write {
        write("{");
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
                    // write ,
                    write(",");
                }
            }
        }
        write("}");
        // write }
    }

    public boolean isHumanReadable() {
        return humanReadable;
    }

    public void setHumanReadable(boolean humanReadable) {
        this.humanReadable = humanReadable;
    }
}
