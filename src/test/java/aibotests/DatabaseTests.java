package aibotests;

import database.SQLiteProvider;
import junit.framework.TestCase;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database tests
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

public final class DatabaseTests extends TestCase {
    private SQLiteProvider database;
    private String testDatabaseName = "test.db";
    private String testTableName = "test_table";
    private String testFieldName = "test_field";
    private String testFieldValue = "test_field_value";

    @Override
    protected void setUp() throws Exception {
        this.database = SQLiteProvider.getNewInstanceForDatabase(this.testDatabaseName);

        super.setUp();
    }

    public void testCreateTable() {
        String createString = String.format("CREATE TABLE IF NOT EXISTS %s(%s text)",
                this.testTableName, this.testFieldName);

        try {
            this.database.executeStatement(createString, false);

            assertTrue(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            assertTrue(false);
        }
    }

    public void testInsertData() {
        String createString = String.format("CREATE TABLE IF NOT EXISTS %s(%s text)",
        this.testTableName, this.testFieldName);

        String insertString = String.format("INSERT INTO %s(%s) VALUES (\"%s\")",
                this.testTableName, this.testFieldName, this.testFieldValue);

        try {
            this.database.executeStatement(createString, false);
            this.database.executeStatement(insertString, false);

            assertTrue(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            assertTrue(false);
        }
    }

    public void testSelectData() {
        String createString = String.format("CREATE TABLE IF NOT EXISTS %s(%s text)",
                this.testTableName, this.testFieldName);

        String insertString = String.format("INSERT INTO %s(%s) VALUES (\"%s\")",
                this.testTableName, this.testFieldName, this.testFieldValue);

        String selectString = String.format("SELECT * from %s t", this.testTableName);

        try {
            this.database.executeStatement(createString, false);
            this.database.executeStatement(insertString, false);

            ResultSet rs = this.database.executeStatement(selectString, true);

            assertTrue(rs.next());

            String testData = rs.getString(1);

            assertEquals(testData, this.testFieldValue);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            assertTrue(false);
        }
    }

    public void testUpdateData() {
        String createString = String.format("CREATE TABLE IF NOT EXISTS %s(%s text)",
                this.testTableName, this.testFieldName);

        String insertString = String.format("INSERT INTO %s(%s) VALUES (\"%s\")",
                this.testTableName, this.testFieldName, this.testFieldValue);

        String updateString = String.format("UPDATE %s SET %s=\"%s\"",
                this.testTableName, this.testFieldName, this.testFieldValue);

        String selectString = String.format("SELECT t.%s from %s t", this.testFieldName, this.testTableName);

        try {
            this.database.executeStatement(createString, false);
            this.database.executeStatement(insertString, false);
            this.database.executeStatement(updateString, false);

            ResultSet rs = this.database.executeStatement(selectString, true);

            assertTrue(rs.next());

            String testData = rs.getString(1);

            assertEquals(testData, this.testFieldValue);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            assertTrue(false);
        }
    }

    public void testDeleteData() {
        String createString = String.format("CREATE TABLE IF NOT EXISTS %s(%s text)",
                this.testTableName, this.testFieldName);

        String insertString = String.format("INSERT INTO %s(%s) VALUES (\"%s\")",
                this.testTableName, this.testFieldName, this.testFieldValue);

        String deleteString = String.format("DELETE FROM  %s", this.testTableName);

        String selectString = String.format("SELECT t.%s from %s t", this.testFieldName, this.testTableName);

        try {
            this.database.executeStatement(createString, false);
            this.database.executeStatement(insertString, false);
            this.database.executeStatement(deleteString, false);

            ResultSet rs = this.database.executeStatement(selectString, true);

            assertFalse(rs.next());
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            assertTrue(false);
        }
    }
}