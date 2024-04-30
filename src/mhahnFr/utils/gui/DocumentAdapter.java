/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2023 - 2024  mhahnFr
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

package mhahnFr.utils.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This class acts as an adapter for the {@link DocumentListener}.
 *
 * @author mhahnFr
 * @since 02.01.23
 */
public class DocumentAdapter implements DocumentListener {
    /**
     * Constructs a {@link DocumentAdapter}.
     */
    public DocumentAdapter() {}

    @Override
    public void insertUpdate(DocumentEvent e) {}

    @Override
    public void removeUpdate(DocumentEvent e) {}

    @Override
    public void changedUpdate(DocumentEvent e) {}
}
