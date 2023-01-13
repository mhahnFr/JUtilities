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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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

    private void readField(Object obj) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        skipWhitespaces();
        final var fieldName = readField();

        final var field = getField(obj, fieldName);
        skipWhitespaces();
        if (stream.peek('{')) {
            // object
            final var value = field.getType().getConstructor().newInstance();
            readInto(value);
        } else if (stream.peek('[')) {
            // collection
        } else if (stream.peek('"')) {
            // string or enum
            final var buffer = readString();
            if (Enum.class.isAssignableFrom(field.getType())) {
                field.set(obj, field.getType().getMethod("valueOf", String.class).invoke(null, buffer));
            } else {
                field.set(obj, buffer);
            }
        } else {
            // raw value
        }
    }

    public void readInto(Object obj) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        skipWhitespaces();
        expect("{");
        readFields(obj);
        skipWhitespaces();
        expect("}");
    }
}
