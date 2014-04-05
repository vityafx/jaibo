package org.jaibo.api.database;

import java.sql.Connection;
import java.sql.DriverManager;

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

class SQLiteProvider extends AbstractDatabaseProvider {

    @Override
    public String getProviderName() {
        return "sqlite";
    }

    @Override
    protected Connection getConnection(DatabaseCredentials credentials) {
        if (credentials != null) {
            String formattedDatabaseName = String.format("jdbc:sqlite:%s.db", credentials.getDatabaseName());

            try {
                if (formattedDatabaseName != null && !formattedDatabaseName.isEmpty()) {
                    Class.forName("org.sqlite.JDBC");

                    return DriverManager.getConnection(formattedDatabaseName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
