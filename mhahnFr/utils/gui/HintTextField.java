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

import javax.swing.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * This class represents a normal {@link JTextField}, extended by a default
 * implementation of a hint that is displayed inside the text-field if no
 * text is entered.
 *
 * @since 04.11.2022
 * @author mhahnFr
 */
public class HintTextField extends JTextField {
    private final String hint;

    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        init();
    }

    public HintTextField(String hint, int columns) {
        super(hint, columns);
        this.hint = hint;
        init();
    }

    public HintTextField(Document doc, String hint, int columns) {
        super(doc, hint, columns);
        this.hint = hint;
        init();
    }

    private void init() {
        addFocusListener(new Listener());
        setForeground(Color.lightGray);
    }

    public String getHint() { return hint; }

    private class Listener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            if (getText().equals(hint)) {
                setText("");
                setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (getText().isBlank()) {
                setText(hint);
                setForeground(Color.lightGray);
            }
        }
    }
}
