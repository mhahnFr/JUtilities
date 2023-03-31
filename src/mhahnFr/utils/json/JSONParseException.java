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

import mhahnFr.utils.StreamPosition;

/**
 * This class acts as exception related to the JSON
 * converting classes.
 *
 * @author mhahnFr
 * @since 23.01.23
 */
public class JSONParseException extends Exception {
    /**
     * Constructs this exception using the given message
     * and {@link StreamPosition}.
     *
     * @param message the error message
     * @param position the point in the stream that caused the error
     */
    public JSONParseException(final String message, final StreamPosition position) {
        super(position.makeErrorText(message));
    }
}
