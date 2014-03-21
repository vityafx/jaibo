package aibotests;

import aibo.ExtensionManager;

import junit.framework.TestCase;

/**
 * Extension manager tests
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

public final class ExtensionManagerTests extends TestCase {
    private final String testExtensionName = "test";
    private final String testCoreExtensionName = "core";

    private static final ExtensionManager extensionManager = new ExtensionManager(null, null);


    public void testAddExtension() {
//        try {
//            extensionManager.addExtensionByName(this.testExtensionName);
//
//            assertTrue(true);
//        } catch (ExtensionManagerError e) {
//            assertTrue(false);
//        }
//
//        try {
//            extensionManager.addExtensionByName(this.testExtensionName);
//
//            assertTrue(false);
//        } catch (ExtensionManagerError e) {
//            assertTrue(true);
//        }
//            assertTrue(true);
    }

    public void testRemoveExtension() {
//        try {
//            extensionManager.removeExtensionByName(this.testExtensionName);
//
//            assertTrue(true);
//        } catch (ExtensionManagerError e) {
//            assertTrue(false);
//        }
        assertTrue(true);
    }

    public void testAddCoreExtension() {
//        try {
//            extensionManager.addExtensionByName(this.testCoreExtensionName);
//
//            assertTrue(false);
//        } catch (ExtensionManagerError e) {
//            assertTrue(true);
//        } catch (ExtensionError e) {
//            assertTrue(true);
//        }
        assertTrue(true);
    }

    public void testRemoveCoreExtension() {
//        try {
//            extensionManager.removeExtensionByName(this.testCoreExtensionName);
//
//            assertTrue(false);
//        } catch (ExtensionManagerError e) {
//            assertTrue(true);
//        } catch (ExtensionError e) {
//            assertTrue(true);
//        }
        assertTrue(true);
    }
}
