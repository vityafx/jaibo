package AIBO;

import java.util.ArrayList;

/**
 * Extension manager realization
 * Copyright (C) 2014  Victor Polevoy (vityatheboss@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ExtensionManager {
    private ArrayList<Runnable> extensions = new ArrayList<Runnable>();


    public ExtensionManager() {

    }

    public ExtensionManager(String[] extensionNames) {
        this.addExtensionsByNames(extensionNames);
    }


    public void addExtensionsByNames(String[] extensionNames) {
        for (String name : extensionNames) {
            this.addExtensionByName(name);
        }
    }

    public void addExtensionByName(String extensionName) {
        Runnable extension = this.findExtensionByName(extensionName);

        if (extension != null) {
            this.extensions.add(extension);
        }
    }

    public void removeExtensionByName(String extensionName) {
        //for
    }

    private Runnable findExtensionByName(String extensionName) {
        return null;
    }
}
