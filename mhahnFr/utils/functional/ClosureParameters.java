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

package mhahnFr.utils.functional;

import mhahnFr.utils.Pair;

import java.util.HashMap;

public class ClosureParameters {
    private final HashMap<String, Object> parameters;

    ClosureParameters(final int count) {
        parameters = HashMap.newHashMap(count);
    }

    void addArgument(final String name, final Object argument) {
        parameters.put(name, argument);
    }

    public Object getArgument(final String name) {
        return parameters.get(name);
    }

    @SafeVarargs
    public final void check(final Pair<String, Class<?>>... parameters) {
        for (final var parameter : parameters) {
            final var isClass = getArgument(parameter.getFirst()).getClass();
            if (!parameter.getSecond().isAssignableFrom(isClass)) {
                throw new IllegalArgumentException("Parameter types do not match! Expected: " + parameter.getSecond() +
                                                   ", got: " + isClass);
            }
        }
    }
}
