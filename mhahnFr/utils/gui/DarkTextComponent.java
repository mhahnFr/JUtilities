/*
 * SecretPathway - A MUD client.
 *
 * Copyright (C) 2022  mhahnFr
 *
 * This file is part of the SecretPathway. This program is free software:
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
 *
 * @param <T> the text component an instance of this class holds
 *
 * @since 04.11.2022
 * @author mhahnFr
 */
public class DarkTextComponent<T extends JTextComponent> extends DarkComponent<T> {
    /**
     * Constructs this wrapper for the given component.
     *
     * @param component the component to wrap around
     */
    public DarkTextComponent(final T component) { this(component, false); }

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
            component.setCaretColor(Color.white);
        } else {
            component.setCaretColor(Color.black);
            component.setBackground(Color.white);
        }
    }
}
