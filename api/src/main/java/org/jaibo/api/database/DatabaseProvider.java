package org.jaibo.api.database;

import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Database provider wrapper
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

// this class actually implements DatabaseProviderInterface but in static context
public final class DatabaseProvider {
    private static AbstractDatabaseProvider currentProvider;

    private static String _defaultDatabase;


    public static void setDatabaseProvider(String databaseProviderName) {
        DatabaseProvider.currentProvider = DatabaseProviderFactory.createDatabaseProvider(databaseProviderName);
    }

    public static void setCredentials(String host, String username, String password, String databaseName) {
        DatabaseCredentials credentials = new DatabaseCredentials(host, username, password, databaseName);

        DatabaseProviderFactory.setCredentials(credentials);
    }

    public static CachedRowSet executeStatement(String sqlStatement, boolean needResult) throws SQLException {
        if (DatabaseProvider.currentProvider != null) {
            return DatabaseProvider.currentProvider.executeStatement(sqlStatement, needResult);
        } else {
            return null;
        }
    }

    public static CachedRowSet executeStatementWithDatabase(DatabaseCredentials credentials, String sqlStatement, boolean needResult)
            throws SQLException {
        if (DatabaseProvider.currentProvider != null) {
            return DatabaseProvider.currentProvider.executeStatementWithDatabase(credentials, sqlStatement, needResult);
        } else {
            return null;
        }
    }

    public static CachedRowSet executePreparedStatement(PreparedStatement preparedStatement,
                                                 boolean needResult) throws SQLException {
        if (DatabaseProvider.currentProvider != null) {
            return DatabaseProvider.currentProvider.executePreparedStatement(preparedStatement, needResult);
        } else {
            return null;
        }
    }

    public static CachedRowSet executePreparedStatementWithDatabase(DatabaseCredentials credentials,
                                                             PreparedStatement preparedStatement,
                                                             boolean needResult) throws SQLException {
        if (DatabaseProvider.currentProvider != null) {
            return DatabaseProvider.currentProvider.executePreparedStatementWithDatabase(
                    credentials,
                    preparedStatement,
                    needResult);
        } else {
            return null;
        }
    }

    public static PreparedStatement createPreparedStatement(String query) {
        if (DatabaseProvider.currentProvider != null) {
            return DatabaseProvider.currentProvider.createPreparedStatement(query);
        } else {
            return null;
        }
    }

    public static PreparedStatement createPreparedStatementWithDatabase(DatabaseCredentials credentials, String query) {
        if (DatabaseProvider.currentProvider != null) {
            return DatabaseProvider.currentProvider.createPreparedStatementWithDatabase(credentials, query);
        } else {
            return null;
        }
    }
}
