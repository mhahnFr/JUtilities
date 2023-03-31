/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2022  mhahnFr
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
 * This class combines two objects. The two objects can be changed at any time.
 *
 * @param <T> the type of the first object
 * @param <O> the type of the second object
 * @author mhahnFr
 * @since 02.09.2022
 */
public class MutablePair<T, O> extends Pair<T, O> {
    /**
     * Constructs an empty pair. The contents can be set using {@link #setFirst(Object)}
     * and {@link #setSecond(Object)}.
     *
     * @see #setFirst(Object)
     * @see #setSecond(Object)
     * @see #link(Object, Object)
     */
    public MutablePair() {
        super(null, null);
    }

    /**
     * Creates a pair consisting of the two given objects.
     *
     * @param first  the first object
     * @param second the second object
     */
    public MutablePair(T first, O second) {
        super(first, second);
    }

    /**
     * Links the two given objects in this instance. If there were previously two
     * objects linked in this instance, they are overwritten.
     *
     * @param first the first object
     * @param second the second object
     */
    public void link(T first, O second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Replaces the first object by the given one.
     *
     * @param first the object to replace the first object of this instance
     */
    public void setFirst(T first) {
        this.first = first;
    }

    /**
     * Replaces the second object by the given one.
     *
     * @param second the object to replace the second object of this instance
     */
    public void setSecond(O second) {
        this.second = second;
    }
}
