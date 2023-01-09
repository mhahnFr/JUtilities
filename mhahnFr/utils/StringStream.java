/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2022 - 2023  mhahnFr
 *
 * This file is part of the JUtilities. This library is free software:
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
        if (chars.length - index <= string.length()) {
            return false;
        }

        return source.substring(index).startsWith(string);
    }

    /**
     * Returns the next character that would be read. It does not
     * check for the bounds, it can (and should) be done using
     * {@link #hasNext()}.
     *
     * @return the next character to be read
     * @see #hasNext()
     * @see #next()
     */
    public char peek() {
        return chars[index];
    }

    /**
     * Reads the given number of characters. The read characters are
     * returned. If attempted to read more characters than are left in the
     * stream, all remaining characters are read and returned. {@link #hasNext()}
     * will then return {@code false}.
     *
     * @param count the number of characters to be read
     * @return an array with the read characters
     */
    public char[] get(int count) {
        if (chars.length - index <= count) {
            count = chars.length - index - 1;
        }
        char[] toReturn = new char[count + 1];
        for (int i = 0; i < count; ++i) {
            toReturn[i] = next();
        }
        return toReturn;
    }
    /**
     * Skips one character. If attempted to skip more characters
     * than are left in the stream, all remaining characters are
     * skipped, {@link #hasNext()} will then return {@code false}.
     *
     * @return the new reading index
     */
    public int skip() {
        return skip(1);
    }

    /**
     * Skips the given amount of characters. If attempted to skip
     * more characters than are left in the stream, all remaining
     * characters are skipped, {@link #hasNext()} will then return
     * {@code false}.
     *
     * @param count the amount of characters to be skipped
     * @return the new reading index
     */
    public int skip(int count) {
        if (chars.length - index <= count) {
            count = chars.length - index;
        }
        return index += count;
    }

    /**
     * Returns the current reading index.
     *
     * @return the current reading index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns and consumes the next character form this stream.
     *
     * @return the next character in the underlying string
     * @see #hasNext()
     */
    public char next() {
        return chars[index++];
    }

    /**
     * Returns whether there is a next character in this stream.
     *
     * @return whether a next character is available
     * @see #next()
     */
    public boolean hasNext() {
        return index < chars.length;
    }
}
