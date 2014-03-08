package database;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;

import aibo.AIBO;
import com.sun.rowset.CachedRowSetImpl;

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
    private static SQLiteProvider DatabaseProvider = null;

    private Connection connection;

    private String databaseName;

    private SQLiteProvider(String databaseName) throws SQLException, ClassNotFoundException {
        this.setDatabaseName(databaseName);

        this.connect();
    }

    public static SQLiteProvider getInstance() {
        if (DatabaseProvider == null) {
            DatabaseProvider = SQLiteProvider.getNewInstanceForDatabase(AIBO.Configuration.get("aibo.database_name"));
        }

        return DatabaseProvider;
    }

    public static SQLiteProvider getNewInstanceForDatabase(String databaseName) {
        try {
            String formattedDatabaseName = String.format("jdbc:sqlite:%s", databaseName);
            SQLiteProvider databaseProvider = null;

            databaseProvider = new SQLiteProvider(formattedDatabaseName);

            return databaseProvider;
        } catch (Exception e) {
            System.out.println("Exception occured while creating database provider object:" + e.getMessage());
        }

        return null;
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

    public CachedRowSet executeStatement(String sqlStatement, boolean needResult)
            throws SQLException {
        CachedRowSet cachedRowSet = null;

        if (sqlStatement != null && !sqlStatement.isEmpty()) {

            Statement statement = this.connection.createStatement();

            if (needResult) {
                cachedRowSet = new CachedRowSetImpl();
                cachedRowSet.populate(statement.executeQuery(sqlStatement));
            } else {
                statement.executeUpdate(sqlStatement);
            }

            statement.close();
        }

        return cachedRowSet;
    }

    @Override
    protected void finalize() throws Throwable {
        this.connection.close();

        super.finalize();
    }
}
