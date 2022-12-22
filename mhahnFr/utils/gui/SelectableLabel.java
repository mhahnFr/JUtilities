/*
 * SecretPathway - A MUD client.
 *
 * Copyright (C) 2018 - 2022  mhahnFr
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

import java.awt.Color;
import java.io.Serial;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * This class creates the surface for labels that can be selected.
 *
 * @author mhahnFr
 * @since 22.01.2018
 */
public abstract class SelectableLabel extends JPanel {
    @Serial
    private static final long serialVersionUID = -2358507279255848384L;
    /**
     * This is the {@link JCheckBox} used to select this label.
     */
    protected JCheckBox checkBox;

    /**
     * Creates a selectable label using the given text.
     *
     * @param text the text to be displayed
     */
    protected SelectableLabel(String text) {
        checkBox = new JCheckBox(text);
    }

    /**
     * Returns the displayed text.
     *
     * @return the text currently displayed on this label
     */
    public String getText() {
        return checkBox.getText();
    }

    /**
     * Sets whether this label should be selected.
     * By default, such labels are not selected.
     *
     * @param b whether this label should be selected
     */
    public void setSelected(boolean b) {
        checkBox.setSelected(b);
    }

    /**
     * Returns whether this label is selected.
     *
     * @return whether this label is selected
     */
    public boolean isSelected() {
        return checkBox.isSelected();
    }

    /**
     * Sets the tooltip for this label.
     *
     * @param text  the string to display; if the text is <code>null</code>,
     *              the tool tip is turned off for this component
     */
    public void setToolTipText(String text) {
        checkBox.setToolTipText(text);
    }

    /**
     * Sets the foreground colour of this label.
     *
     * @param fg  the desired foreground <code>Color</code>
     */
    public void setForeground(Color fg) {
        if(checkBox != null) {
            checkBox.setForeground(fg);
        }
    }
}
