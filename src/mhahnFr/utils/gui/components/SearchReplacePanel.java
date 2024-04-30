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

package mhahnFr.utils.gui.components;

import mhahnFr.utils.gui.DarkModeListener;
import mhahnFr.utils.gui.DocumentAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class acts as a search and replace panel. It can be easily be
 * used by passing the {@link JTextComponent} in order for this class
 * to handle the searching and the replacing process.
 * <br><br>
 * Otherwise, the {@link Listener} can be used to hook to the functionality
 * provided by this component.
 *
 * @author mhahnFr
 * @since 17.04.23
 */
public class SearchReplacePanel extends JPanel implements DarkModeListener {
    /** The list with all components, enabling the dark mode on them.           */
    private final java.util.List<DarkComponent<? extends JComponent>> components = new ArrayList<>();
    /** The list with the registered {@link Listener}s.                         */
    private final java.util.List<Listener> listeners = new ArrayList<>();
    /** The {@link JTextField} used as main search field.                       */
    private final JTextField searchField;
    /** The {@link JTextField} used as replace field.                           */
    private final JTextField replaceField;
    /** The panel with the controls used for replacing text.                    */
    private final JPanel replaceControls;
    /** The {@link JCheckBox} used for displaying the replace section.          */
    private final JCheckBox replaceBox;
    /** Indicates whether the replace section should be active.                 */
    private boolean replace;
    /** The {@link JTextComponent} that should be controlled by this component. */
    private JTextComponent installed;
    /** The {@link Document} of the {@link #installed installed component.}     */
    private Document document;
    /** The {@link String} for which to search.                                 */
    private String searching = "";

    /**
     * Constructs this UI component.
     */
    public SearchReplacePanel() {
        this(null);
    }

    /**
     * Constructs this UI component. Installs the given {@link JTextComponent}.
     *
     * @param install the component to be installed
     */
    public SearchReplacePanel(final JTextComponent install) {
        super(new BorderLayout());
        components.add(new DarkComponent<>(this));
            final var fields = new DarkComponent<>(new JPanel(new BorderLayout()), components).getComponent();
                searchField = new DarkTextComponent<>(new HintTextField("Search..."), components).getComponent();
                searchField.addActionListener(__ -> selectNext());

                replaceField = new DarkTextComponent<>(new HintTextField("Replace..."), components).getComponent();
                replaceField.addActionListener(__ -> replaceCurrent());
            fields.add(searchField,  BorderLayout.NORTH);
            fields.add(replaceField, BorderLayout.SOUTH);

            final var controls = new DarkComponent<>(new JPanel(new BorderLayout()), components).getComponent();
                final var searchControls = new DarkComponent<>(new JPanel(), components).getComponent();
                searchControls.setLayout(new BoxLayout(searchControls, BoxLayout.X_AXIS));
                    final var previousButton = new JButton("^");
                    previousButton.addActionListener(__ -> selectPrevious());

                    final var nextButton = new JButton("v");
                    nextButton.addActionListener(__ -> selectNext());

                    final var allButton = new JButton("Mark all");
                    allButton.addActionListener(__ -> selectAll());

                    replaceBox = new DarkComponent<>(new JCheckBox("Replace"), components).getComponent();
                    replaceBox.addItemListener(__ -> setReplaceImpl(replaceBox.isSelected()));
                searchControls.add(previousButton);
                searchControls.add(nextButton);
                searchControls.add(allButton);
                searchControls.add(replaceBox);

                replaceControls = new DarkComponent<>(new JPanel(), components).getComponent();
                    final var replaceButton = new JButton("Replace");
                    replaceButton.addActionListener(__ -> replaceCurrent());

                    final var replaceAllButton = new JButton("Replace all");
                    replaceAllButton.addActionListener(__ -> replaceAll());
                replaceControls.add(replaceButton);
                replaceControls.add(replaceAllButton);
            controls.add(searchControls,  BorderLayout.NORTH);
            controls.add(replaceControls, BorderLayout.SOUTH);
        add(fields,   BorderLayout.CENTER);
        add(controls, BorderLayout.EAST);

        install(install);
        setReplaceImpl(false);
        searchField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                selectAll();
                searching = searchField.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                selectAll();
                searching = searchField.getText();
            }
        });
    }

    /**
     * Returns the whole text of the installed component.
     *
     * @return the whole text of the installed component
     * @see #document
     */
    private String getAllText() {
        try {
            return document.getText(0, document.getLength());
        } catch (BadLocationException | NullPointerException __) {
            return "";
        }
    }

    /**
     * Selects the previous search result. Calls {@link Listener#selectPrevious()}.
     */
    private void selectPrevious() {
        listeners.forEach(Listener::selectPrevious);

        if (installed != null && document != null) {
            final var text = getAllText();

            final var startIndex = installed.getSelectionStart();
            var begin = text.lastIndexOf(searching, startIndex > 0 ? startIndex - 1 : text.length());
            if (begin < 0) {
                begin = text.lastIndexOf(searching);
                if (begin < 0) return;
            }
            installed.setCaretPosition(begin);
            installed.moveCaretPosition(begin + searching.length());
        }
    }

    /**
     * Selects the next search occurrence. Calls {@link Listener#selectNext()}.
     *
     * @return whether a search result was found
     */
    private boolean selectNext() {
        listeners.forEach(Listener::selectNext);

        if (installed != null && document != null) {
            final var text = getAllText();

            var begin = text.indexOf(searching, installed.getCaretPosition());
            if (begin < 0) {
                begin = text.indexOf(searching);
                if (begin < 0) return false;
            }
            installed.setCaretPosition(begin);
            installed.moveCaretPosition(begin + searching.length());
        }
        return true;
    }

    /**
     * Selects all search occurrences. Calls {@link Listener#selectAll()}.
     */
    private void selectAll() {
        listeners.forEach(Listener::selectAll);
    }

    /**
     * Replaces the currently selected search occurrence
     * or the next occurrence.<br>
     * Calls {@link Listener#replaceCurrent()}.
     */
    private void replaceCurrent() {
        listeners.forEach(Listener::replaceCurrent);

        final var replacing = getReplaceString();
        if (document != null && installed != null && replacing != null && !replacing.isBlank()) {
            if (!Objects.equals(installed.getSelectedText(), searching)) {
                selectNext();
            }
            if (installed.getSelectedText() != null) {
                installed.replaceSelection(replacing);
            }
        }
    }

    /**
     * Replaces all occurrences of the searched string.
     * Calls {@link Listener#replaceAll()}.
     */
    private void replaceAll() {
        listeners.forEach(Listener::replaceAll);

        final var replacing = getReplaceString();
        if (installed != null && document != null && replacing != null && !replacing.isBlank()) {
            while (selectNext()) {
                replaceCurrent();
            }
        }
    }

    /**
     * Activates or deactivates the replacement mode.
     *
     * @param replace whether to activate the replacement mode
     */
    private void setReplaceImpl(final boolean replace) {
        this.replace = replace;

        replaceField.setVisible(replace);
        replaceControls.setVisible(replace);
    }

    /**
     * Returns the {@link String} which is searched.
     *
     * @return the searched string
     */
    public String getSearchString() {
        return searching;
    }

    /**
     * Returns the replacement string. If the replacement
     * mode is not active, {@code null} is returned.
     *
     * @return the replacement string
     * @see #isReplace()
     */
    public String getReplaceString() {
        return replace ? replaceField.getText() : null;
    }

    /**
     * Installs the given {@link JTextComponent} to be controlled
     * by this component. If there was previously a {@link JTextComponent}
     * installed, the old one is ignored and the given one will be
     * controlled instead.
     *
     * @param installed the component to be installed
     */
    public void install(final JTextComponent installed) {
        this.installed = installed;
        this.document  = installed == null ? null : installed.getDocument();
    }

    /**
     * Activates or deactivates the replacement mode.
     *
     * @param replace whether to enable the replacement mode
     */
    public void setReplace(final boolean replace) {
        replaceBox.setSelected(replace);
    }

    /**
     * Returns whether the replacement mode is currently
     * active.
     *
     * @return whether the replacement mode is active
     */
    public boolean isReplace() {
        return replace;
    }

    @Override
    public void darkModeToggled(final boolean dark) {
        for (final var component : components) {
            component.setDark(dark);
        }
    }

    /**
     * Adds the given {@link Listener}.
     *
     * @param listener the listener to be added
     */
    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the given {@link Listener}.
     *
     * @param listener the listener to be removed
     */
    public void removeListener(final Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);

        if (aFlag) {
            searchField.requestFocusInWindow();
        }
    }

    /**
     * This interface defines the function hooks available for
     * the {@link SearchReplacePanel}.
     * <br><br>
     * If not all functionality should be hooked, consider using
     * the {@link Adapter}.
     *
     * @author mhahnFr
     * @since 17.04.23
     */
    public interface Listener {
        /**
         * Called when the previous occurrence of the searched string
         * should be highlighted.
         */
        void selectPrevious();

        /**
         * Called when the next occurrence of the searched string
         * should be highlighted.
         */
        void selectNext();

        /**
         * Called when all occurrences of the searched string
         * should be highlighted.
         */
        void selectAll();

        /**
         * Called when the currently highlighted search result
         * should be replaced by the replacement string.<br>
         * If no result is highlighted, the next search result
         * should be highlighted and replaced.
         */
        void replaceCurrent();

        /**
         * Called when all occurrences of the searched string
         * should be replaced. Should do nothing if the searched
         * string is not found.
         */
        void replaceAll();

        /**
         * This class acts as an adapter for the {@link Listener}.
         *
         * @author mhahnFr
         * @since 17.04.23
         */
        class Adapter implements Listener {
            /**
             * Constructs an {@link Adapter} for the {@link Listener} of
             * the {@link SearchReplacePanel}.
             */
            public Adapter() {}

            @Override
            public void selectPrevious() {}
            @Override
            public void selectNext() {}
            @Override
            public void selectAll() {}
            @Override
            public void replaceCurrent() {}
            @Override
            public void replaceAll() {}
        }
    }
}
