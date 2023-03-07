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

/**
 * This class represents a closure runtime object. After a callee
 * has been set using either the {@link #Closure(ClosureCallee) constructor}
 * or {@link #setCallee(ClosureCallee)} and the parameter types have been
 * passed using {@link #bind(Pair[])}, it can be called using {@link #call(Object...)}.
 * <br>
 * The parameters passed need to be in the correct order, they can be accessed
 * by the callee by their name. The types are checked when the closure is
 * called using {@link #call(Object...)}.
 *
 * @param <T> the return type of the closure
 * @author mhahnFr
 * @since 06.03.23
 */
public class Closure<T> {
    /** The callee.                                                   */
    private ClosureCallee<T> callee;
    /** The parameters, they are passed in the order they are stored. */
    private Pair<String, Class<?>>[] parameterTypes;

    /**
     * Constructs an empty closure. Before it can be used,
     * the parameter types need to be {@link #bind(Pair[]) bound},
     * the callee needs to be set ({@link #setCallee(ClosureCallee)}.
     * <br>
     * Then, it can be {@link #call(Object...) called}.
     *
     * @see #setCallee(ClosureCallee)
     * @see #bind(Pair[])
     * @see #call(Object...)
     */
    public Closure() {}

    /**
     * Constructs a closure using the given callee.
     * <br>
     * Before it can be used, the parameter types need to be
     * {@link #bind(Pair[]) bound}.
     *
     * @param callee the callee to be called by this closure
     * @see #bind(Pair[])
     * @see #call(Object...)
     */
    public Closure(final ClosureCallee<T> callee) {
        this.callee = callee;
    }

    /**
     * Sets the callee called by the method {@link #call(Object...)}.
     * If a callee was previously set, it is replaced.
     *
     * @param callee the new callee
     * @return this object
     */
    public Closure<T> setCallee(final ClosureCallee<T> callee) {
        this.callee = callee;
        return this;
    }

    /**
     * Binds this closure to the given parameters. The parameters
     * are a mapping of types and names, the order in which they are
     * passed is retained. The closure can be rebound at any time,
     * but the arguments passed to {@link #call(Object...)} need to
     * be of the same types and in the same order as passed to this
     * function.
     * <br>
     * Only after this function has been called, the closure is ready
     * to call the set callee (see {@link #setCallee(ClosureCallee)})
     * using the method {@link #call(Object...)}.
     *
     * @param parameterTypes the names and types of the parameters
     * @return this object
     */
    @SafeVarargs
    public final Closure<T> bind(Pair<String, Class<?>>... parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }

    /**
     * Calls the set callee with the given parameters. The given arguments
     * need to be in the order as their types have been passed to {@link #bind(Pair[])}.
     *
     * @param arguments the arguments to be passed
     * @return the result of the callee
     * @throws NullPointerException if no callee is set, this closure has not been bound or {@code null} is passed as argument
     * @throws IllegalArgumentException if the passed parameters do not match to the bound types
     * @see #setCallee(ClosureCallee)
     * @see #bind(Pair[])
     */
    public T call(Object... arguments) {
        if (parameterTypes == null) {
            throw new NullPointerException("Closure has not been bound! Hint: Call Closure<T>::bind(Pair<String, Class<?>>...) first.");
        }
        if (arguments == null) {
            throw new NullPointerException("Null passed as argument! Hint: Consider wrapping it into an array: new Object[] { null }.");
        }
        if (callee == null) {
            throw new NullPointerException("No callee set for closure!");
        }
        if (parameterTypes.length != arguments.length) {
            throw new IllegalArgumentException("Argument count does not equal parameter count!");
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
