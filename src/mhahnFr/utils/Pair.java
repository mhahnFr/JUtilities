/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2017 - 2024  mhahnFr
 *
 * This file is part of the JUtilities.
 *
 * JUtilities is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JUtilities is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * JUtilities, see the file LICENSE.  If not, see <https://www.gnu.org/licenses/>.
 */

package mhahnFr.utils;

/**
 * This class combines to given objects.
 *
 * @param <T> the first type
 * @param <O> the second type
 * @author mhahnFr
 * @since 20.12.2017
 */
public class Pair<T, O> {
    /**
     * The first object.
     */
    protected T first;
    /**
     * The second object.
     */
    protected O second;

    /**
     * Creates a pair consisting of the two given objects.
     *
     * @param first the first object
     * @param second the second object
     */
    public Pair(T first, O second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first object of this instance.
     *
     * @return the first object of type {@code T}
     */
    public T getFirst() {
        return first;
    }

    /**
     * Returns the second object of this instance.
     *
     * @return the second object of type {@code O}
     */
    public O getSecond() {
        return second;
    }

    /**
     * Returns the object that is paired to the given one. If the given object is
     * none of the two objects of this instance, null is returned.
     *
     * @param linked the object whose pair should be returned
     * @return the paired object of the given object
     */
    public Object getThePairedValue(Object linked) {
        if(linked.equals(first)) {
            return second;
        } else if(linked.equals(second)) {
            return first;
        }
        return null;
    }

    public String toString() {
        return first.toString() + " <-> " + second.toString();
    }
}