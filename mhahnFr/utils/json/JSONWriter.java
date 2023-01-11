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

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

/**
 * This class writes objects into a JSON-format.
 *
 * @author mhahnFr
 * @since 11.01.23
 */
public class JSONWriter {
    private List<Field> getFields(final Object obj) {
        return null;
    }

    private boolean isPrimitive(final Class<?> c) {
        return false;
    }

    private void writePrimitive(OutputStream out, final Field field, final Object obj) {
        // write "name": <primitive>
    }

    private void writeObject(OutputStream out, final Field field, final Object obj) throws IllegalAccessException {
        // Write "name":
        // if is collection / dictionary: write [, otherwise {
        writeTo(out, field.get(obj));
        // if was collection / dictionary: write ] or }
    }

    public void writeTo(OutputStream out, Object obj) throws IllegalAccessException {
        // write {
        final var it = getFields(obj).iterator();
        while (it.hasNext()) {
            final var field = it.next();
            if (isPrimitive(field.getType())) {
                writePrimitive(out, field, obj);
            } else {
                writeObject(out, field, obj);
            }
            if (it.hasNext()) {
                // write ,
            }
        }
        // write }
    }
}
