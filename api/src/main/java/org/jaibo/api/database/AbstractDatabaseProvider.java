package org.jaibo.api.database;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;

/**
 * Abstract database provider class
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

abstract class AbstractDatabaseProvider implements DatabaseProviderInterface{
    private DatabaseCredentials databaseCredentials;

    protected AbstractDatabaseProvider() {
    }

    public abstract String getProviderName();

    public DatabaseCredentials getDatabaseCredentials() {
        return databaseCredentials;
    }

    public void setDatabaseCredentials(DatabaseCredentials databaseCredentials) {
        this.databaseCredentials = databaseCredentials;
    }

    protected Connection getConnection() {
        return this.getConnection(this.getDatabaseCredentials());
    }

    protected abstract Connection getConnection(DatabaseCredentials credentials);

    public CachedRowSet executeStatement(String sqlStatement, boolean needResult)
            throws SQLException {

        return executeStatementWithDatabase(this.getDatabaseCredentials(), sqlStatement, needResult);
    }

    public CachedRowSet executeStatementWithDatabase(DatabaseCredentials credentials, String sqlStatement, boolean needResult)
            throws SQLException {
        CachedRowSet cachedRowSet = null;
        Connection sqlConnection = getConnection(credentials);

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

    public CachedRowSet executePreparedStatement(PreparedStatement preparedStatement, boolean needResult)
            throws SQLException {
        return executePreparedStatementWithDatabase(this.getDatabaseCredentials(),
                preparedStatement, needResult);
    }

    public CachedRowSet executePreparedStatementWithDatabase(DatabaseCredentials credentials,
                                                                    PreparedStatement preparedStatement,
                                                                    boolean needResult)
            throws SQLException {

        CachedRowSet cachedRowSet = null;
        Connection sqlConnection = getConnection(credentials);

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

    public PreparedStatement createPreparedStatement(String query) {
        return createPreparedStatementWithDatabase(this.getDatabaseCredentials(), query);
    }

    public PreparedStatement createPreparedStatementWithDatabase(DatabaseCredentials credentials, String query) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = getConnection(credentials).prepareStatement(query);
        } catch (SQLException e) {
            System.out.println("Exception occured while creating prepared statement: " + e.getMessage());
        }

        return preparedStatement;
    }
}
