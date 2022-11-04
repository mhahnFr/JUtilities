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

package mhahnFr.utils.gui;

import javax.swing.JComponent;

import java.awt.Color;

/**
 * This class is a wrapper around a given {@link JComponent} to add a universal
 * way to enable a dark mode on the given component.
 *
 * @param <T> the component an instance of this class holds
 *
 * @since 04.11.2022
 * @author mhahnFr
 */
public class DarkComponent<T extends JComponent> {
    /** The underlying component.                            */
    protected final T component;
    /** Indicates whether the dark mode is currently active. */
    private boolean dark;

    /**
     * Constructs this wrapper for the given component.
     *
     * @param component the component to wrap around
     */
    public DarkComponent(final T component) { this(component, false); }

    /**
     * Constructs this wrapper for the given component. The {@code boolean} value
     * indicates whether the given component is already in the dark mode.
     *
     * @param component the component to wrap around
     * @param dark      whether the given component is already in the dark mode
     */
    public DarkComponent(final T component, final boolean dark) { this.component = component; this.dark = dark; }

    /**
     * Changes the mode the underlying component is displayed. If the underlying
     * component is already in the given mode, nothing happens.
     *
     * @param dark whether the underlying component should appear in the dark mode
     */
    public void setDark(final boolean dark) {
        if (this.dark != dark) {
            this.dark = dark;
            if (dark) {
                component.setForeground(Color.white);
                component.setBackground(Color.DARK_GRAY);
            } else {
                component.setForeground(null);
                component.setBackground(null);
            }
        }
    }
}
