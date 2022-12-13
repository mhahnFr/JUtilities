/*
 * JUtilities - Some utilities written for Java.
 *
 * Copyright (C) 2022  mhahnFr
 *
 * This file is part of the JVMScript. This program is free software:
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

package mhahnFr.utils.gui.menu;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the requirements a menu controlled window must have.
 *
 * @author mhahnFr
 * @since 12.12.22
 */
public abstract class MenuFrame extends JFrame {
    /** A list with all active menu frames. */
    private static final List<MenuFrame> menuFrames = new ArrayList<>();

    /**
     * Returns the window that has the focus. If no such
     * window is found, {@code null} is returned.
     *
     * @return the focused window
     */
    public static MenuFrame getFocusedWindow() {
        for (final var window : menuFrames) {
            if (window.isFocusOwner()) {
                return window;
            }
        }
        return null;
    }

    /**
     * Disposes all active menu frames.
     */
    public static void closeAll() {
        for (final var window : menuFrames) {
            window.dispose();
        }
    }

    /**
     * Asks each active menu frame to close itself. The process stops
     * at the first menu frame vetoing against it.
     *
     * @return whether all windows have been closed
     */
    public static boolean vetoableCloseAll() {
        final var tempList = new ArrayList<>(menuFrames);

        for (final var window : tempList) {
            if (!window.vetoableDispose()) {
                return false;
            } else {
                window.dispose();
            }
        }
        return true;
    }

    /**
     * Initializes this menu frame. It is added to the list of
     * the active windows.
     */
    public MenuFrame() {
        init();
    }

    /**
     * Initializes this menu frame. It is added to the list of
     * the active windows.
     *
     * @param title the title this window should have
     */
    public MenuFrame(String title) {
        super(title);

        init();
    }

    /**
     * Initializes this window.
     */
    private void init() {
        menuFrames.add(this);
    }

    /**
     * Called when this window should be closed. It can veto against,
     * if for example unsaved changes still need to be saved.
     *
     * @return whether this window may be disposed
     */
    protected abstract boolean vetoableDispose();

    @Override
    public void dispose() {
        super.dispose();

        menuFrames.remove(this);
    }
}
