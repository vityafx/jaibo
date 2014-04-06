package dataservertests;

import org.jaibo.api.dataserver.DataServerInfoObject;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Data server info object tests
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

public final class InfoObjectTests extends TestCase {
    private final DataServerInfoObject infoObject = new DataServerInfoObject();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        infoObject.clear();
    }

    public void testOneNode() {
        infoObject.putData("key", "value");

        assertEquals(infoObject.toString(), "{\"key\":\"value\"}");
    }

    public void testSimpleArrayNode() {
        ArrayList<String> stringArray = new ArrayList<String>();
        stringArray.add("value1");
        stringArray.add("value2");

        infoObject.putArrayWithEscaping("collection", stringArray);

        assertEquals(infoObject.toString(), "{\"collection\":[\"value1\",\"value2\"]}");
    }

    public void testComplexArrayNode() {
        ArrayList<DataServerInfoObject> objectArray = new ArrayList<DataServerInfoObject>();
        DataServerInfoObject obj1 = new DataServerInfoObject();
        obj1.putData("key1", "value1");
        DataServerInfoObject obj2 = new DataServerInfoObject();
        obj2.putData("key2", "value2");
        objectArray.add(obj1);
        objectArray.add(obj2);

        infoObject.putArrayWithoutEscaping("collection", objectArray);

        assertEquals(infoObject.toString(), "{\"collection\":[{\"key1\":\"value1\"},{\"key2\":\"value2\"}]}");
    }

    public void testSelfSubPackage() {
        infoObject.putData("key", "value");
        infoObject.putSubObject("subObject", infoObject);

        assertEquals(infoObject.toString(), "{\"subObject\":{\"key\":\"value\"},\"key\":\"value\"}");
    }

    public void testNewSubPackage() {
        DataServerInfoObject newObject = new DataServerInfoObject();
        newObject.putData("newKey", "newValue");
        infoObject.putData("key", "value");
        infoObject.putSubObject("subObject", newObject);

        assertEquals(infoObject.toString(), "{\"subObject\":{\"newKey\":\"newValue\"},\"key\":\"value\"}");
    }
}
