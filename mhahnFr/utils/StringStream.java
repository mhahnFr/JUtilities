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
    /** The current position of the stream. */
    private int index;

    /**
     * Constructs this stream using the given source text.
     *
     * @param source the source text
     */
    public StringStream(final String source) {
        this.source = source;
    }
}
