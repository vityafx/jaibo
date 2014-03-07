package database;

import java.sql.*;

/**
 * SQLite database provider
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

public final class SQLiteProvider {
    private Connection connection;

    private String databaseName;


    public SQLiteProvider(String databaseName) throws SQLException, ClassNotFoundException {
        this.setDatabaseName(databaseName);

        this.connect();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = String.format("jdbc:sqlite:%s", databaseName);
    }

    public void connect() throws SQLException, ClassNotFoundException {
        if (this.databaseName != null && !this.databaseName.isEmpty()) {
            Class.forName("org.sqlite.JDBC");

            this.connection = DriverManager.getConnection(this.databaseName);

            System.out.println("Opened database file successfully");
        }
    }

    public void executeStatement(String sqlStatement, boolean commitAfterExecuting) throws SQLException {
        if (sqlStatement != null && !sqlStatement.isEmpty()) {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(sqlStatement);
            statement.close();
        }
    }
}
