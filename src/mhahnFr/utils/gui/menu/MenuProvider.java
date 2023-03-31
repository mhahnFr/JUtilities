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
import java.awt.event.ActionListener;

/**
 * This class defines the functionality a menu generator must have.
 * Instances of subclasses can be installed as menu providers in the
 * {@link MenuFactory}.
 *
 * @author mhahnFr
 * @since 13.12.22
 */
public abstract class MenuProvider {
    /**
     * Convenience access to the registered default About action
     * of the {@link MenuFactory}.
     *
     * @see MenuFactory#defaultAboutAction()
     * @see MenuFactory#setAboutAction(Runnable)
     */
    protected void defaultAboutAction() {
        MenuFactory.getInstance().defaultAboutAction();
    }

    /**
     * Convenience access to the registered default Settings action
     * of the {@link MenuFactory}.
     *
     * @see MenuFactory#defaultSettingsAction()
     * @see MenuFactory#setSettingsAction(Runnable)
     */
    protected void defaultSettingsAction() {
        MenuFactory.getInstance().defaultSettingsAction();
    }

    /**
     * Called by the {@link MenuFactory} when a new menu-bar has to be
     * created. The given {@link ActionListener} shall be used as listener
     * for the menu-items.
     *
     * @param listener the listener for the menu-items
     * @return a newly generated menu-bar
     */
    public abstract JMenuBar generateMenuBar(ActionListener listener);
}
