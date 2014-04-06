package dataservertests;

import junit.framework.TestCase;
import org.jaibo.api.dataserver.DataServerInfoObject;
import org.jaibo.api.dataserver.DataServerInfoPackage;
import org.jaibo.api.dataserver.DataServerInfoStatusCode;
import org.jaibo.api.errors.DataServerError;

/**
 * Data server info package tests
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

public final class InfoPackageTests extends TestCase {
    private final DataServerInfoPackage infoPackage = new DataServerInfoPackage();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        infoPackage.clear();
    }

    public void testSimpleDataWithSuccessStatus() {
        DataServerInfoObject infoObject = new DataServerInfoObject();
        infoObject.putData("key", "value");

        this.infoPackage.setInfoObject(infoObject);

        assertEquals(this.infoPackage.toString(), "{\"status\":\"Success\",\"data\":{\"key\":\"value\"}}");
    }

    public void testSimpleDataWithCustomStatusesStatus() {
        DataServerInfoObject infoObject = new DataServerInfoObject();
        infoObject.putData("key", "value");

        this.infoPackage.setInfoObject(infoObject);

        assertEquals(this.infoPackage.toString(), "{\"status\":\"Success\",\"data\":{\"key\":\"value\"}}");

        try {
            this.infoPackage.setStatusMessage("error");

            assertTrue(false);
        } catch (DataServerError e) {
            assertTrue(true);
        }

        this.infoPackage.setStatus(DataServerInfoStatusCode.ARGUMENT_ERROR);

        assertEquals(this.infoPackage.toString(), "{\"status\":\"Argument error\"}");

        this.infoPackage.setStatusMessage("error");

        assertEquals(this.infoPackage.toString(), "{\"status\":\"Argument error: error\"}");


        this.infoPackage.setStatus(DataServerInfoStatusCode.PARSE_ERROR);

        assertEquals(this.infoPackage.toString(), "{\"status\":\"Parse error\"}");

        this.infoPackage.setStatusMessage("error");

        assertEquals(this.infoPackage.toString(), "{\"status\":\"Parse error: error\"}");


        this.infoPackage.setStatus(DataServerInfoStatusCode.INCORRECT_PATH);

        assertEquals(this.infoPackage.toString(), "{\"status\":\"Incorrect path\"}");

        try {
            this.infoPackage.setStatusMessage("error");

            assertTrue(false);
        } catch (DataServerError e) {
            assertTrue(true);
        }
    }
}