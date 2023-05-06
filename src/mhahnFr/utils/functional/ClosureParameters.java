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

/**
 * This class represents the parameters passed to a closure (callee).
 * The parameter types can be checked using {@link #check(Pair[])}.
 * The actual arguments are retrieved by their name: {@link #getArgument(String)}.
 *
 * @author mhahnFr
 * @since 06.03.23
 * @see Closure
 * @see ClosureCallee
 */
public class ClosureParameters {
    /** The backing of the arguments mapped to their name. */
    private final HashMap<String, Object> parameters;

    /**
     * Constructs this parameter abstraction using the given
     * amount of parameters.
     *
     * @param count the count of arguments to be held
     */
    ClosureParameters(final int count) {
//        parameters = HashMap.newHashMap(count); // Will be activated on Java 21. - mhahnFr
        parameters = new HashMap<>();
    }

    /**
     * Adds an argument by its name.
     *
     * @param name     the name of the argument
     * @param argument the actual value of the argument
     */
    void addArgument(final String name, final Object argument) {
        parameters.put(name, argument);
    }

    /**
     * Returns the argument mapped to the given name.
     *
     * @param name the name of the requested argument
     * @return the passed value
     * @throws IllegalArgumentException if no parameter with the given name exists
     */
    public Object getArgument(final String name) {
        if (!parameters.containsKey(name)) {
            throw new IllegalArgumentException("No such parameter! (" + name + ")");
        }
        return parameters.get(name);
    }

    /**
     * Checks the stored arguments. If the stored arguments with the
     * given name do not match the given {@link Class}, an exception
     * is thrown.
     *
     * @param parameters the names and types of the requested parameters
     */
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
