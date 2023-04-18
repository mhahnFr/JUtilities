/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2023  mhahnFr
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

public class SearchReplacePanel extends JPanel implements DarkModeListener {
    private final java.util.List<DarkComponent<? extends JComponent>> components = new ArrayList<>();
    private final java.util.List<Listener> listeners = new ArrayList<>();
    private final JTextField replaceField;
    private final JPanel replaceControls;
    private final JCheckBox replaceBox;
    private boolean replace;
    private JTextComponent installed;
    private Document document;
    private String searching = "";

    public SearchReplacePanel() {
        this(null);
    }

    public SearchReplacePanel(final JTextComponent install) {
        super(new BorderLayout());
        components.add(new DarkComponent<>(this));
            final var fields = new DarkComponent<>(new JPanel(new BorderLayout()), components).getComponent();
                final var searchField = new DarkTextComponent<>(new HintTextField("Search..."), components).getComponent();
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

    private String getAllText() {
        try {
            return document.getText(0, document.getLength());
        } catch (BadLocationException | NullPointerException __) {
            return "";
        }
    }

    private void selectPrevious() {
        listeners.forEach(Listener::selectPrevious);

        if (installed != null && document != null) {
            final var text = getAllText();

            var begin = text.lastIndexOf(searching, installed.getSelectionEnd());
            if (begin < 0) {
                begin = text.lastIndexOf(searching);
                if (begin < 0) return;
            }
            installed.setCaretPosition(begin);
            installed.moveCaretPosition(begin + searching.length());
        }
    }

    private void selectNext() {
        listeners.forEach(Listener::selectNext);

        if (installed != null && document != null) {
            final var text = getAllText();

            var begin = text.indexOf(searching, installed.getCaretPosition());
            if (begin < 0) {
                begin = text.indexOf(searching);
                if (begin < 0) return;
            }
            installed.setCaretPosition(begin);
            installed.moveCaretPosition(begin + searching.length());
        }
    }

    private void selectAll() {
        listeners.forEach(Listener::selectAll);
    }

    private void replaceCurrent() {
        listeners.forEach(Listener::replaceCurrent);

        final var replacing = getReplaceString();
        if (document != null && installed != null && replacing != null && !replacing.isBlank()) {
            if (!Objects.equals(installed.getSelectedText(), searching)) {
                selectNext();
            }
            installed.replaceSelection(replacing);
        }
    }

    private void replaceAll() {
        listeners.forEach(Listener::replaceAll);
    }

    private void setReplaceImpl(final boolean replace) {
        this.replace = replace;

        replaceField.setVisible(replace);
        replaceControls.setVisible(replace);
    }

    public String getSearchString() {
        return searching;
    }

    public String getReplaceString() {
        return replace ? null : replaceField.getText();
    }

    public void install(final JTextComponent installed) {
        this.installed = installed;
        this.document  = installed == null ? null : installed.getDocument();
    }

    public void setReplace(final boolean replace) {
        replaceBox.setSelected(replace);
    }

    public boolean isReplace() {
        return replace;
    }

    @Override
    public void darkModeToggled(final boolean dark) {
        for (final var component : components) {
            component.setDark(dark);
        }
    }

    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(final Listener listener) {
        listeners.remove(listener);
    }

    public interface Listener {
        void selectPrevious();
        void selectNext();
        void selectAll();

        void replaceCurrent();
        void replaceAll();

        class Adapter implements Listener {
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
