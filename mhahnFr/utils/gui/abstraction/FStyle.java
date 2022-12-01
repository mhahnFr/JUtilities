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
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.Color;
import java.util.Objects;

/**
 * This class represents Styles for use in text components.
 *
 * @author mhahnFr
 * @since 14.04.2022
 */
public class FStyle {
    /** Indicates whether to use a bold font.          */
    private Boolean bold;
    /** Indicates whether to use an italic font.       */
    private Boolean italic;
    /** Indicates whether to use a strike-trough font. */
    private Boolean strike;
    /** Indicates whether to use an underlined font.   */
    private Boolean underlined;
    /** The alignment of the font.                     */
    private Integer alignment;
    /** The bidi-level of the font.                    */
    private Integer bidiLevel;
    /** The text size.                                 */
    private Integer size;
    /** The first line indent of the font.             */
    private Float firstLineIndent;
    /** The name of the font family.                   */
    private String family;
    /** The background colour to be used.              */
    private Color background;
    /** The foreground color to be used.               */
    private Color foreground;
    /** The parent style.                              */
    private FStyle parent;
    /** A cached, native style.                        */
    private Style cached;

    /**
     * Constructs an empty style.
     */
    public FStyle() {
        this((FStyle) null);
    }

    /**
     * Constructs a style, copying the given native one.
     *
     * @param style the native style to be copied
     */
    public FStyle(Style style) {
        this();
        setForeground(StyleConstants.getForeground(style));
        setBackground(StyleConstants.getBackground(style));
        setBold(StyleConstants.isBold(style));
        setItalic(StyleConstants.isItalic(style));
        setStrikeThrough(StyleConstants.isStrikeThrough(style));
        setUnderlined(StyleConstants.isUnderline(style));
        setAlignment(StyleConstants.getAlignment(style));
        setBidiLevel(StyleConstants.getBidiLevel(style));
        setSize(StyleConstants.getFontSize(style));
        setFirstLineIndent(StyleConstants.getFirstLineIndent(style));
        setFamily(StyleConstants.getFontFamily(style));
    }

    /**
     * Copies the given style. If the inheritance is not resolved,
     * this style will have the same parent as the given one.
     *
     * @param original the style to be copied
     * @param resolveInheritance whether to resolve the inheritance
     */
    public FStyle(FStyle original, boolean resolveInheritance) {
        if (resolveInheritance) {
            setParent(null);
            bold = original.isBold();
            italic = original.isItalic();
            strike = original.isStrikeThrough();
            underlined = original.isUnderlined();
            alignment = original.getAlignment();
            bidiLevel = original.getBidiLevel();
            size = original.getSize();
            firstLineIndent = original.getFirstLineIndent();
            family = original.getFamily();
            background = original.getBackground();
            foreground = original.getForeground();
        } else {
            setParent(original.getParent());
            bold = original.bold;
            italic = original.italic;
            strike = original.strike;
            underlined = original.underlined;
            alignment = original.alignment;
            bidiLevel = original.bidiLevel;
            size = original.size;
            firstLineIndent = original.firstLineIndent;
            family = original.family;
            background = original.background;
            foreground = original.foreground;
        }
        cached = null;
    }

    /**
     * Creates an empty style, using the given style as parent.
     *
     * @param parent the parent style
     */
    public FStyle(FStyle parent) {
        setParent(parent);
        bold = null;
        italic = null;
        strike = null;
        underlined = null;
        alignment = null;
        bidiLevel = null;
        size = null;
        firstLineIndent = null;
        family = null;
        background = null;
        foreground = null;
        cached = null;
    }

    /**
     * Returns the parent style of this one.
     *
     * @return the parent style
     */
    public FStyle getParent() {
        return parent;
    }

    /**
     * Returns whether this style inherits the given one. Returns {@code true}
     * if the given style is a parent somewhere in the inheritance tree.
     *
     * @param other the style to test whether it is a parent
     * @return whether the given style is in the inheritance list of this one
     */
    private boolean inherits(FStyle other) {
        if (getParent() == null) return false;
        if (getParent() == other) return true;
        return getParent().inherits(other);
    }

    /**
     * Sets the parent style. If the inheritance to the given parent would
     * lead to a cyclic inheritance, an {@link IllegalArgumentException} is
     * thrown and the parent remains untouched. Invalids the cache if successful.
     *
     * @param parent the new parent style
     * @throws IllegalArgumentException when the new inheritance tree consists of this style
     */
    public void setParent(FStyle parent) {
        final FStyle oldParent = this.parent;
        this.parent = parent;
        if (inherits(this)) {
            this.parent = oldParent;
            throw new IllegalArgumentException("A FStyle must not inherit itself!");
        }
        cached = null;
    }

    /**
     * Returns whether to use a bold font.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return whether to use a bold font
     */
    public Boolean isBold() {
        return parent != null && bold == null ? parent.isBold() : bold;
    }

    /**
     * Sets the bold property if it is not the same.
     * Invalidates the cache.
     *
     * @param bold the new bold property
     */
    public void setBold(Boolean bold) {
        if (!Objects.equals(this.bold, bold)) {
            this.bold = bold;
            cached = null;
        }
    }

    /**
     * Returns whether the bold property is overwritten by this style.
     *
     * @return whether the bold property is overwritten
     */
    public boolean isBoldOverwritten() {
        return bold != null;
    }

    /**
     * Returns the italic property stored in this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the italic property stored in this style
     */
    public Boolean isItalic() {
        return parent != null && italic == null ? parent.isItalic() : italic;
    }

    /**
     * Sets the italic property if it is not the same.
     * Invalidates the cache.
     *
     * @param italic the new italic property
     */
    public void setItalic(Boolean italic) {
        if (!Objects.equals(this.italic, italic)) {
            this.italic = italic;
            cached = null;
        }
    }

    /**
     * Returns whether the italic property is overwritten by this style.
     *
     * @return whether the italic property is overwritten
     */
    public boolean isItalicOverwritten() {
        return italic != null;
    }

    /**
     * Returns the strike-through property of this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the strike-through property stored in this style
     */
    public Boolean isStrikeThrough() {
        return parent != null && strike == null ? parent.isStrikeThrough() : strike;
    }

    /**
     * Sets the strike-through property if it is not the same.
     * Invalidates the cache.
     *
     * @param strike the new strike-through property
     */
    public void setStrikeThrough(Boolean strike) {
        if (!Objects.equals(this.strike, strike)) {
            this.strike = strike;
            cached = null;
        }
    }

    /**
     * Returns whether the strike-through property is overwritten by this style.
     *
     * @return whether the strike-through property is overwritten
     */
    public boolean isStrikeThroughOverwritten() {
        return strike != null;
    }

    /**
     * Returns the underline property of this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the underline property
     */
    public Boolean isUnderlined() {
        return parent != null && underlined == null ? parent.isUnderlined() : underlined;
    }

    /**
     * Sets the underline property if it is not the same.
     * Invalidates the cache.
     *
     * @param underlined the new underline property
     */
    public void setUnderlined(Boolean underlined) {
        if (!Objects.equals(this.underlined, underlined)) {
            this.underlined = underlined;
            cached = null;
        }
    }

    /**
     * Returns whether the underline property is overwritten by this style.
     *
     * @return whether the underline property is overwritten
     */
    public boolean isUnderlinedOverwritten() {
        return underlined != null;
    }

    /**
     * Returns the alignment property of this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the alignment property
     */
    public Integer getAlignment() {
        return parent != null && alignment == null ? parent.getAlignment() : alignment;
    }

    /**
     * Sets the alignment property if it is not the same.
     * Invalidates the cache.
     *
     * @param alignment the new alignment property
     */
    public void setAlignment(Integer alignment) {
        if (!Objects.equals(this.alignment, alignment)) {
            this.alignment = alignment;
            cached = null;
        }
    }

    /**
     * Returns whether the alignment property is overwritten by this style.
     *
     * @return whether the alignment property is overwritten
     */
    public boolean isAlignmentOverwritten() {
        return alignment != null;
    }

    /**
     * Returns the bidi-level of this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the bidi-level property
     */
    public Integer getBidiLevel() {
        return parent != null && bidiLevel == null ? parent.getBidiLevel() : bidiLevel;
    }

    /**
     * Sets the bidi-level property if it is not the same.
     * Invalidates the cache.
     *
     * @param bidiLevel the new bidi-level property
     */
    public void setBidiLevel(Integer bidiLevel) {
        if (!Objects.equals(this.bidiLevel, bidiLevel)) {
            this.bidiLevel = bidiLevel;
            cached = null;
        }
    }

    /**
     * Returns whether the bidi-level property is overwritten by this style.
     *
     * @return whether the bidi-level is overwritten
     */
    public boolean isBidiLevelOverwritten() {
        return bidiLevel != null;
    }

    /**
     * Returns the font size of this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the font size of this style
     */
    public Integer getSize() {
        return parent != null && size == null ? parent.getSize() : size;
    }

    /**
     * Sets the font size if it is not the same.
     * Invalidates the cache.
     *
     * @param size the font size of this style
     */
    public void setSize(Integer size) {
        if (!Objects.equals(this.size, size)) {
            this.size = size;
            cached = null;
        }
    }

    /**
     * Returns whether the font size is overwritten by this style.
     *
     * @return whether the size is overwritten
     */
    public boolean isSizeOverwritten() {
        return size != null;
    }

    /**
     * Returns the first line indent property of this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the first line indent property of this style
     */
    public Float getFirstLineIndent() {
        return parent != null && firstLineIndent == null ? parent.getFirstLineIndent() : firstLineIndent;
    }

    /**
     * Sets the first line indent property if it is not the same.
     * Invalidates the cache.
     *
     * @param firstLineIndent the new first line indent property
     */
    public void setFirstLineIndent(Float firstLineIndent) {
        if (!Objects.equals(this.firstLineIndent, firstLineIndent)) {
            this.firstLineIndent = firstLineIndent;
            cached = null;
        }
    }

    /**
     * Returns whether the first line indent property is overwritten by this style.
     *
     * @return whether the first line indent property is overwritten
     */
    public boolean isFirstLineIndentOverwritten() {
        return firstLineIndent != null;
    }

    /**
     * Returns the font family name stored in this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the font family name stored in this style
     */
    public String getFamily() {
        return parent != null && family == null ? parent.getFamily() : family;
    }

    /**
     * Sets the font family name if it is not the same.
     * Invalidates the cache.
     *
     * @param family the new font family name
     */
    public void setFamily(String family) {
        if (!Objects.equals(this.family, family)) {
            this.family = family;
            cached = null;
        }
    }

    /**
     * Returns whether the font family name is overwritten by this style.
     *
     * @return whether the font family name is overwritten
     */
    public boolean isFamilyOverwritten() {
        return family != null;
    }

    /**
     * Returns the background colour stored in this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the background colour stored in this style
     */
    public Color getBackground() {
        return parent != null && background == null ? parent.getBackground() : background;
    }

    /**
     * Sets the background colour if it is not the same.
     * Invalidates the cache.
     *
     * @param background the new background colour
     */
    public void setBackground(Color background) {
        if (!Objects.equals(background, this.background)) {
            this.background = background;
            cached = null;
        }
    }

    /**
     * Returns whether the background colour is overwritten by this style.
     *
     * @return whether the background colour is overwritten
     */
    public boolean isBackgroundOverwritten() {
        return background != null;
    }

    /**
     * Returns the foreground colour stored in this style.
     * Attempts to resolve the inheritance if it is not overwritten.
     *
     * @return the foreground colour stored in this style
     */
    public Color getForeground() {
        return parent != null && foreground == null ? parent.getForeground() : foreground;
    }

    /**
     * Sets the foreground colour if it is not the same.
     * Invalidates the cache.
     *
     * @param foreground the new foreground colour
     */
    public void setForeground(Color foreground) {
        if (!Objects.equals(foreground, this.foreground)) {
            this.foreground = foreground;
            cached = null;
        }
    }

    /**
     * Returns whether the foreground colour is overwritten by this style.
     *
     * @return whether the foreground colour is overwritten
     */
    public boolean isForegroundOverwritten() {
        return foreground != null;
    }

    /**
     * Constructs a native style using the information stored in this style.
     * Inheritance is resolved in order to construct the style properly.
     *
     * @param parent the native style to be used as parent for the newly constructed one
     * @return a native style constructed from the information stored in this style
     */
    public Style asStyle(Style parent) {
        if (cached == null) {
            cached = StyleContext.getDefaultStyleContext().addStyle(null, parent);
            if (isBold() != null) StyleConstants.setBold(cached, isBold());
            if (isItalic() != null) StyleConstants.setItalic(cached, isItalic());
            if (isUnderlined() != null) StyleConstants.setUnderline(cached, isUnderlined());
            if (isStrikeThrough() != null) StyleConstants.setStrikeThrough(cached, isStrikeThrough());
            if (getAlignment() != null) StyleConstants.setAlignment(cached, getAlignment());
            if (getBidiLevel() != null) StyleConstants.setBidiLevel(cached, getBidiLevel());
            if (getSize() != null) StyleConstants.setFontSize(cached, getSize());
            if (getFirstLineIndent() != null) StyleConstants.setFirstLineIndent(cached, getFirstLineIndent());
            if (getBackground() != null) StyleConstants.setBackground(cached, getBackground());
            if (getForeground() != null) StyleConstants.setForeground(cached, getForeground());
            if (getFamily() != null) StyleConstants.setFontFamily(cached, getFamily());
        }
        return cached;
    }

    @Override
    public String toString() {
        var toReturn = "FStyle: [ ";

        if (bold != null) toReturn += (bold + ", ");
        if (italic != null) toReturn += (italic + ", ");
        if (underlined != null) toReturn += (underlined + ", ");
        if (strike != null) toReturn += (strike + ", ");
        if (foreground != null) toReturn += (foreground + ", ");
        if (background != null) toReturn += (background + ", ");

        return toReturn + " ]";
    }

    /**
     * Returns whether a cached, native style is available.
     *
     * @return whether cached style is available
     */
    protected boolean hasCached() {
        return cached != null;
    }

    /**
     * Invalidates the cache.
     */
    protected void invalidateCache() {
        cached = null;
    }
}