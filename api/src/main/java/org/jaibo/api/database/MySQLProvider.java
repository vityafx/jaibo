package org.jaibo.api.database;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;

/**
 * MySQL database provider
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

final class MySQLProvider extends AbstractDatabaseProvider {

    @Override
    public String getProviderName() {
        return "mysql";
    }

    @Override
    protected Connection getConnection(DatabaseCredentials credentials) {
        if (credentials != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");

                return DriverManager.getConnection(
                        credentials.getMysqlUrlString(),
                        credentials.getUsername(),
                        credentials.getPassword());

            } catch (Exception e) {
                System.out.println("Can't find mysql jdbc driver.");
            }
        }

        return null;
    }
}
