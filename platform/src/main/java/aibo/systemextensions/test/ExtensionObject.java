package aibo.systemextensions.test;

import org.jaibo.api.Extension;

/**
 * Test extension object
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

public class ExtensionObject extends Extension {

    public ExtensionObject() {
        System.out.println("Test extension created");
    }
    @Override
    public void run() {

    }

    @Override
    public String getExtensionName() {
        return "test";
    }

    @Override
    public String getExtensionVersion() {
        return "1.0";
    }

    @Override
    public void setCommands() {
    }

    @Override
    public String getHelpPage() {
        return "";
    }

    @Override
    protected void beforeUnload() {
        System.out.println("Test extension will be unloaded");
    }
}
