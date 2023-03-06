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

public class Closure<T> {
    private ClosureCallee<T> callee;
    private Pair<String, Class<?>>[] parameterTypes;

    public Closure() {}

    public Closure(final ClosureCallee<T> callee) {
        this.callee = callee;
    }

    public Closure<T> setCallee(final ClosureCallee<T> callee) {
        this.callee = callee;
        return this;
    }

    @SafeVarargs
    public final Closure<T> bind(Pair<String, Class<?>>... parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }

    public T call(Object... arguments) {
        if (parameterTypes.length != arguments.length) {
            throw new IllegalArgumentException("Argument count does not equal parameter count!");
        }
        if (callee == null) {
            throw new NullPointerException("No callee set for closure!");
        }

        final var callArguments = new ClosureParameters(parameterTypes.length);
        for (int i = 0; i < parameterTypes.length; ++i) {
            if (!parameterTypes[i].getSecond().isAssignableFrom(arguments[i].getClass())) {
                throw new IllegalArgumentException("Argument types do not match!");
            }
            callArguments.addArgument(parameterTypes[i].getFirst(), arguments[i]);
        }
        return callee.call(callArguments);
    }
}
