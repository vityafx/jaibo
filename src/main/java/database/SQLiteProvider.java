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
    private static Connection getConnection(String databaseName) {
        try {
            if (databaseName != null && !databaseName.isEmpty()) {
                Class.forName("org.sqlite.JDBC");

                Connection connection = DriverManager.getConnection(databaseName);

                return connection;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CachedRowSet executeStatement(String sqlStatement, boolean needResult)
            throws SQLException {

        return executeStatementWithDatabase(AIBO.Configuration.get("aibo.database_name"), sqlStatement, needResult);
    }

    public static CachedRowSet executeStatementWithDatabase(String databaseName, String sqlStatement, boolean needResult)
            throws SQLException {
        String formattedDatabaseName = String.format("jdbc:sqlite:%s", databaseName);

        CachedRowSet cachedRowSet = null;
        Connection sqlConnection = getConnection(formattedDatabaseName);

        if (sqlConnection != null && sqlStatement != null && !sqlStatement.isEmpty()) {
            Statement statement = sqlConnection.createStatement();

            if (needResult) {
                cachedRowSet = new CachedRowSetImpl();
                cachedRowSet.populate(statement.executeQuery(sqlStatement));
            } else {
                statement.executeUpdate(sqlStatement);
            }

            statement.close();
            sqlConnection.close();
        }

        return cachedRowSet;
    }

    public static CachedRowSet executePreparedStatement(PreparedStatement preparedStatement, boolean needResult)
            throws SQLException {
        return executePreparedStatementWithDatabase(AIBO.Configuration.get("aibo.database_name"),
                preparedStatement, needResult);
    }

    public static CachedRowSet executePreparedStatementWithDatabase(String databaseName,
                                                                    PreparedStatement preparedStatement,
                                                                    boolean needResult)
            throws SQLException {

        String formattedDatabaseName = String.format("jdbc:sqlite:%s", databaseName);

        CachedRowSet cachedRowSet = null;
        Connection sqlConnection = getConnection(formattedDatabaseName);

        if (sqlConnection != null && preparedStatement != null) {
            if (needResult) {
                cachedRowSet = new CachedRowSetImpl();
                cachedRowSet.populate(preparedStatement.executeQuery());
            } else {
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
            sqlConnection.close();
        }

        return cachedRowSet;
    }

    public static PreparedStatement createPreparedStatement(String query) {
        return createPreparedStatementWithDatabase(AIBO.Configuration.get("aibo.database_name"), query);
    }

    public static PreparedStatement createPreparedStatementWithDatabase(String databaseName, String query) {
        String formattedDatabaseName = String.format("jdbc:sqlite:%s", databaseName);
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = getConnection(formattedDatabaseName).prepareStatement(query);
        } catch (SQLException e) {
            System.out.println("Exception occured while creating prepared statement: " + e.getMessage());
        }

        return preparedStatement;
    }
}
