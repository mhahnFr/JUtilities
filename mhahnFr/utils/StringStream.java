/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2022  mhahnFr
 *
 * This file is part of the JVMScript. This program is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program, see the file LICENSE.  If not, see <https://www.gnu.org/licenses/>.
 */

package mhahnFr.utils;

/**
 * This class enables a string to be streamed.
 *
 * @author mhahnFr
 * @since 27.12.22
 */
public class StringStream {
    /** The source text.                    */
    private final String source;
    /** The characters of the source text.  */
    private final char[] chars;
    /** The current position of the stream. */
    private int index;

    /**
     * Constructs this stream using the given source text.
     *
     * @param source the source text
     */
    public StringStream(final String source) {
        this.source = source;
        this.chars  = this.source.toCharArray();
    }

    /**
     * Returns whether the next character to be read is equal to the
     * given one.
     *
     * @param c the char to be tested
     * @return whether the next character to be read equals the given one
     */
    public boolean peek(char c) {
        return chars[index] == c;
    }

    /**
     * Returns whether the next characters to be read are equal to
     * the given {@link String}.
     *
     * @param string the string to be tested
     * @return whether the next characters to be read equal the string
     */
    public boolean peek(final String string) {
        return source.substring(index).startsWith(string);
    }

    /**
     * Returns and consumes the next character form this stream.
     *
     * @return the next character in the underlying string
     */
    public char next() {
        return chars[index++];
    }
}
