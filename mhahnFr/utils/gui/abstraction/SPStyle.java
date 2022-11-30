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

package mhahnFr.utils.gui.abstraction;

import javax.swing.text.Style;
import java.awt.Color;
import java.awt.Font;

/**
 * This class represents Styles for use in text components.
 *
 * @author mhahnFr
 * @since 30.11.2022
 */
public class SPStyle {
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean striken;
    private Color foreground;
    private Color background;
    private Font base;

    public Boolean isBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Boolean isItalic() {
        return italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean isUnderlined() {
        return underlined;
    }

    public void setUnderlined(Boolean underlined) {
        this.underlined = underlined;
    }

    public Boolean isStriken() {
        return striken;
    }

    public void setStriken(Boolean striken) {
        this.striken = striken;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Font getBaseFont() {
        return base;
    }

    public void setBaseFont(Font base) {
        this.base = base;
    }

    public Style toNative() {
        return null;
    }
}
