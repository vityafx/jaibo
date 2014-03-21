import junit.framework.TestCase;
import org.jaibo.api.database.DatabaseProvider;

import java.sql.PreparedStatement;
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
    private String testDatabaseName = "test.db";
    private String testTableName = "test_table";
    private String testFieldName = "test_field";
    private String testFieldValue = "test_field_value";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateTable() {
        String createString = String.format("CREATE TABLE IF NOT EXISTS %s(%s text)",
                this.testTableName, this.testFieldName);

        try {
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, createString, false);

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
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, createString, false);
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, insertString, false);

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
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, createString, false);
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, insertString, false);

            ResultSet rs = DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, selectString, true);

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
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, createString, false);
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, insertString, false);
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, updateString, false);

            ResultSet rs = DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, selectString, true);

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
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, createString, false);
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, insertString, false);
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, deleteString, false);

            ResultSet rs = DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, selectString, true);

            assertFalse(rs.next());
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            assertTrue(false);
        }
    }

    public void testInsertWithPreparedStatement() {
        String createString = String.format("CREATE TABLE IF NOT EXISTS %s(%s text)",
                this.testTableName, this.testFieldName);

        PreparedStatement preparedInsertStatement = DatabaseProvider.createPreparedStatementWithDatabase(this.testDatabaseName,
                String.format("INSERT INTO %s(%s) VALUES (?)", this.testTableName, this.testFieldName));

        String selectString = String.format("SELECT t.%s from %s t", this.testFieldName, this.testTableName);

        try {
            preparedInsertStatement.setString(1, this.testFieldValue);

            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, createString, false);

            DatabaseProvider.executePreparedStatementWithDatabase(this.testDatabaseName,
                    preparedInsertStatement, false);

            ResultSet rs = DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, selectString, true);

            assertTrue(rs.next());

            String testData = rs.getString(1);

            assertEquals(testData, this.testFieldValue);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            assertTrue(false);
        }
    }

    public void testSelectWithPreparedStatement() {
        String createString = String.format("CREATE TABLE IF NOT EXISTS %s(%s text)",
                this.testTableName, this.testFieldName);

        String insertString = String.format("INSERT INTO %s(%s) VALUES (\"%s\")",
                this.testTableName, this.testFieldName, this.testFieldValue);

        PreparedStatement preparedSelectStatement = DatabaseProvider.createPreparedStatementWithDatabase(this.testDatabaseName,
                String.format("SELECT * from %s t", this.testTableName));

        try {
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, createString, false);
            DatabaseProvider.executeStatementWithDatabase(this.testDatabaseName, insertString, false);

            ResultSet rs = DatabaseProvider.executePreparedStatementWithDatabase(this.testDatabaseName,
                    preparedSelectStatement, true);

            assertTrue(rs.next());

            String testData = rs.getString(1);

            assertEquals(testData, this.testFieldValue);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            assertTrue(false);
        }
    }
}