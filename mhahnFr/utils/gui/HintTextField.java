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
    /** The color that is used when displaying the hint text.                       */
    private static final Color HINT_COLOR = Color.lightGray;
    /** The hint that is displayed when this text field is empty and without focus. */
    private final String hint;

    /**
     * Constructs a normal {@link JTextField}. The given hint is immediately
     * displayed in this text field.
     *
     * @param hint the hint to be used
     * @see #hint
     */
    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        init();
    }

    /**
     * Constructs a normal {@link JTextField}. The given hint is immediately
     * displayed in this text field.
     *
     * @param hint the hint to be used
     * @param columns the number of columns this text field should be capable to hold
     * @see #hint
     */
    public HintTextField(final String hint, int columns) {
        super(hint, columns);
        this.hint = hint;
        init();
    }

    /**
     * Constructs a normal {@link JTextField}. The given hint is immediately
     * displayed in this text field.
     *
     * @param doc the document to be used
     * @param hint the hint to be used
     * @param columns the number of columns this text files should be capable to hold
     * @see #hint
     */
    public HintTextField(Document doc, final String hint, int columns) {
        super(doc, hint, columns);
        this.hint = hint;
        init();
    }

    /**
     * Some initializing stuff that is shared among the constructors.
     */
    private void init() {
        addFocusListener(new Listener());
        setForeground(HINT_COLOR);
    }

    /**
     * Returns the hint that is associated with this text field.
     *
     * @return the hint of this text field
     */
    public String getHint() { return hint; }

    /**
     * Returns whether the hint is currently displayed.
     *
     * @return whether the hint is showing
     * @see #getHint()
     */
    public boolean isShowingHint() { return super.getText().equals(hint) && getForeground().equals(HINT_COLOR); }

    @Override
    public String getText() {
        return isShowingHint() ? "" : super.getText();
    }

    /**
     * This class provides the {@link FocusListener} needed to display the
     * hint of the {@link HintTextField} correctly.
     *
     * @since 04.11.2022
     * @author mhahnFr
     */
    private class Listener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            if (isShowingHint()) {
                setText("");
                setForeground(null);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (HintTextField.super.getText().isBlank()) {
                setText(hint);
                setForeground(HINT_COLOR);
            }
        }
    }
}
