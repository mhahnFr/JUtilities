/*
 * JVMScript - Scripting language powered by JVM's reflection.
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

import javax.swing.JMenuBar;
import java.awt.*;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitResponse;

/**
 * This class constructs menu-bars.
 *
 * @author mhahnFr
 * @since 12.12.22
 */
public class MenuFactory {
    /** Indicates whether quitting is handled globally.      */
    private boolean quit;
    /** Indicates whether about is handled globally.         */
    private boolean about;
    /** Indicates whether the settings are handled globally. */
    private boolean settings;
    /** Indicates whether the menu-bar is globally present.  */
    private final boolean menubar;
    /** The action for About.                                */
    private Runnable aboutAction;
    /** The settings action.                                 */
    private Runnable settingsAction;
    /** The one and only instance of this class.             */
    private static MenuFactory instance;

    /**
     * Returns the one and only instance of this class, Initializes it
     * if necessary.
     *
     * @return the one and only instance
     */
    public static MenuFactory getInstance() {
        if (instance == null) {
            instance = new MenuFactory();
        }
        return instance;
    }

    /**
     * Initializes this factory. Adds global menu actions if available.
     */
    private MenuFactory() {
        if (Desktop.getDesktop().isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
            Desktop.getDesktop().setQuitHandler(this::defaultQuitAction);
            quit = true;
        }
        if (Desktop.getDesktop().isSupported(Desktop.Action.APP_ABOUT)) {
            Desktop.getDesktop().setAboutHandler(__ -> defaultAboutAction());
            about = true;
        }
        if (Desktop.getDesktop().isSupported(Desktop.Action.APP_PREFERENCES)) {
            Desktop.getDesktop().setPreferencesHandler(__ -> defaultSettingsAction());
            settings = true;
        }
        menubar = Desktop.getDesktop().isSupported(Desktop.Action.APP_MENU_BAR);
    }

    /**
     * The default quit action: tries to close all known active windows.
     *
     * @param __ ignored
     * @param response the response
     */
    private void defaultQuitAction(QuitEvent __, QuitResponse response) {
        if (!MenuFrame.vetoableCloseAll()) {
            response.cancelQuit();
        }
        response.performQuit();
    }

    /**
     * The default about action: calls the registered {@link Runnable}.
     *
     * @see #aboutAction
     */
    private void defaultAboutAction() {
        if (aboutAction != null) {
            aboutAction.run();
        }
    }

    /**
     * The default settings action: calls the registered {@link Runnable}.
     *
     * @see #settingsAction
     */
    private void defaultSettingsAction() {
        if (settingsAction != null) {
            settingsAction.run();
        }
    }

    /**
     * Creates and returns a new menu-bar. The given listener is used for
     * the menu-items.
     *
     * @param frame the frame to add the menu-bar to
     * @return a new menu-bar
     */
    public final JMenuBar createMenuBar(MenuFrame frame) {
        // TODO: Implement

        return null;
    }

    /**
     * Registers the given {@link Runnable} to be called when the About menu
     * is triggered. Passing {@code null} as parameter removes the currently
     * registered {@link Runnable}.
     *
     * @param aboutAction the action to be called
     */
    public final void setAboutAction(final Runnable aboutAction) {
        this.aboutAction = aboutAction;
    }

    /**
     * Registers the given {@link Runnable} to be called when the settings menu
     * is triggered. Passing {@code null} as parameter removes the currently
     * registered {@link Runnable}.
     *
     * @param settingsAction the action to be called
     */
    public final void setSettingsAction(final Runnable settingsAction) {
        this.settingsAction = settingsAction;
    }
}
