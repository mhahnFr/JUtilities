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

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchReplacePanel extends JPanel implements DarkModeListener {
    private final java.util.List<DarkComponent<? extends JComponent>> components = new ArrayList<>();
    private final java.util.List<Listener> listeners = new ArrayList<>();
    private final JTextField replaceField;
    private final JPanel replaceControls;
    private boolean replace;

    public SearchReplacePanel() {
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

                    final var replaceBox = new DarkComponent<>(new JCheckBox("Replace"), components).getComponent();
                    replaceBox.addItemListener(__ -> setReplace(replaceBox.isSelected()));
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
    }

    private void selectPrevious() {
        // TODO
    }

    private void selectNext() {
        // TODO
    }

    private void selectAll() {
        // TODO
    }

    private void replaceCurrent() {
        // TODO
    }

    private void replaceAll() {
        // TODO
    }

    public void setReplace(final boolean replace) {
        this.replace = replace;

        replaceField.setVisible(replace);
        replaceControls.setVisible(replace);
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

    }
}
