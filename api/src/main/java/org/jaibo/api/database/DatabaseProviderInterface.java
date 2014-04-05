package org.jaibo.api.database;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database provider interface
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

public interface DatabaseProviderInterface {
    public CachedRowSet executeStatement(String sqlStatement, boolean needResult) throws SQLException;

    public CachedRowSet executeStatementWithDatabase(DatabaseCredentials credentials, String sqlStatement, boolean needResult)
            throws SQLException;

    public CachedRowSet executePreparedStatement(PreparedStatement preparedStatement, boolean needResult)
            throws SQLException;

    public CachedRowSet executePreparedStatementWithDatabase(DatabaseCredentials credentials,
                                                             PreparedStatement preparedStatement,
                                                             boolean needResult)
            throws SQLException;

    public PreparedStatement createPreparedStatement(String query);

    public PreparedStatement createPreparedStatementWithDatabase(DatabaseCredentials credentials, String query);
}
