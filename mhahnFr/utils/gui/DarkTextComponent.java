/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2022 - 2023  mhahnFr
 *
 * This file is part of the JUtilities. This library is free software:
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

package mhahnFr.utils.gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.Color;
import java.util.Collection;

/**
 * This class is a wrapper around a given {@link JTextComponent} to add a
 * universal way to enable a dark mode on the given component.
 * <br><br>
 * Using a list of {@link DarkComponent}s ({@code var list = new ArrayList<DarkComponent<? extends JComponent>>()}),
 * the traditional way of creating a {@link JTextComponent}:<br>
 * {@code var textField = new JTextField("Hello World!")}<br>
 * can become<br>
 * {@code var textField = new DarkTextComponent<>(new JTextField("Hello World!"), list).getComponent()},<br>
 * allowing to toggle the dark mode by simply iterating through that list:<br>
 * {@code for (var darkComponent : list) { darkComponent.setDark(true); } }<br>
 * and preserving the flexibility of Java's Swing framework.
 *
 * @param <T> the text component an instance of this class holds
 *
 * @since 04.11.2022
 * @author mhahnFr
 */
public class DarkTextComponent<T extends JTextComponent> extends DarkComponent<T> {
    /** The color to be used as dark color. */
    protected static final Color darkColor = new Color(36, 36, 36);

    /**
     * Constructs this wrapper for the given component.
     *
     * @param component the component to wrap around
     */
    public DarkTextComponent(final T component) { this(component, false); }

    /**
     * Constructs this wrapper for the given component and appends itself to
     * the given collection of {@link DarkComponent}s.
     *
     * @param component the component to wrap around
     * @param collection the collection to append itself to
     */
    public DarkTextComponent(final T component, Collection<DarkComponent<? extends JComponent>> collection) {
        super(component, collection);
    }

    /**
     * Constructs this wrapper for the given component. The {@code boolean} value
     * indicates whether the given component is already in the dark mode.
     *
     * @param component the component to wrap around
     * @param dark      whether the given component is already in the dark mode
     */
    public DarkTextComponent(final T component, final boolean dark) { super(component, dark); }

    @Override
    public void setDark(final boolean dark) {
        super.setDark(dark);
        if (dark) {
            component.setBackground(darkColor);
            component.setCaretColor(Color.white);
        } else {
            component.setCaretColor(Color.black);
            component.setBackground(Color.white);
        }
    }
}
