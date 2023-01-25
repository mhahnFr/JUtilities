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

package mhahnFr.utils;

/**
 * This record represents a position in a {@link StringStream}.
 *
 * @param position the position in the text
 * @param context the text in which this position is found
 * @author mhahnFr
 * @since 23.01.23
 */
public record StreamPosition(int position, String context) {
    /**
     * Creates an error message pointing to the error found
     * in the text.
     *
     * @param message the message to be displayed
     * @return a descriptive error message
     */
    public String makeErrorText(final String message) {
        int lineBegin, lineEnd;

        for (lineBegin = position - 1; lineBegin >= 0                && context.charAt(lineBegin) != '\n'; --lineBegin);
        for (lineEnd   = position;     lineEnd   <  context.length() && context.charAt(lineEnd)   != '\n'; ++lineEnd);

        int lines = 1;
        for (int i = 0; i < lineBegin + 1; ++i) {
            if (context.charAt(i) == '\n') {
                ++lines;
            }
        }

        final var lineInfo = "\n" + lines + ": ";

        return lineInfo + context.substring(lineBegin + 1, lineEnd) + "\n" + " ".repeat(lineInfo.length() - 1 + Math.max(0, position - lineBegin - 1)) + "^ " + message + "\n";
    }
}
