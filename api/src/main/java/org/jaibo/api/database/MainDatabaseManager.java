package org.jaibo.api.database;

import org.jaibo.api.database.DatabaseProvider;

import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Main shared extension database manager
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

public final class MainDatabaseManager {
    private String adminTableName = "aibo_admins";
    private String adminHostField = "host";
    private String adminExtensionField = "extension";

    public MainDatabaseManager() {
        this.syncTables();
    }

    public void syncTables() {
        this.createAdminTable();
    }

    private void createAdminTable() {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s(%s text, %s text)",
                this.adminTableName, this.adminHostField, this.adminExtensionField);

        try {
            DatabaseProvider.executeStatement(query, false);
        } catch (SQLException e) {
            System.out.println("Failed to create admins table: " + e.getMessage());
        }
    }

    public boolean isAdminExists(String extensionName, String host) {
        if (host != null && !host.isEmpty()) {
            String query = String.format("SELECT COUNT(1) as adminCount FROM %s WHERE %s=? and %s=?",
                    this.adminTableName, this.adminHostField, this.adminExtensionField);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, host);
                preparedStatement.setString(2, extensionName);

                CachedRowSet rowSet = DatabaseProvider.executePreparedStatement(preparedStatement, true);

                if (rowSet != null) {
                    if (rowSet.next()) {
                        int adminCount = rowSet.getInt("adminCount");

                        return adminCount == 1;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Failed to fetch admins list: " + e.getMessage());
            }
        }

        return false;
    }

    public String[] adminsForExtension(String extensionName) {
        if (extensionName != null && !extensionName.isEmpty()) {
            String query = String.format("SELECT %s as host FROM %s WHERE %s=?",
                    this.adminHostField, this.adminTableName, this.adminExtensionField);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatement(query);
            ArrayList<String> adminsList = new ArrayList<String>();
            String[] admins = new String[]{};

            try {
                preparedStatement.setString(1, extensionName);

                CachedRowSet rowSet = DatabaseProvider.executePreparedStatement(preparedStatement, true);

                if (rowSet != null) {
                    while (rowSet.next()) {
                        adminsList.add(rowSet.getString("host"));
                    }
                }

                return adminsList.toArray(admins);
            } catch (SQLException e) {
                System.out.println("Failed to fetch admins list: " + e.getMessage());
            }
        }

        return null;
    }

    public void removeAdmin(String extensionName, String host) {
        if (host != null && !host.isEmpty()) {
            String query = String.format("DELETE FROM %s WHERE %s=? AND %s=?", this.adminTableName,
                    this.adminHostField, this.adminExtensionField);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, host);
                preparedStatement.setString(2, extensionName);

                DatabaseProvider.executePreparedStatement(preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to remove admin (host=%s): %s", host, e.getMessage()));
            }
        }
    }

    public void addAdmin(String extensionName, String host) {
        if (host != null && !host.isEmpty()) {
            String query = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)", this.adminTableName,
                    this.adminHostField, this.adminExtensionField);
            PreparedStatement preparedStatement = DatabaseProvider.createPreparedStatement(query);

            try {
                preparedStatement.setString(1, host);
                preparedStatement.setString(2, extensionName);

                DatabaseProvider.executePreparedStatement(preparedStatement, false);
            } catch (SQLException e) {
                System.out.println(String.format("Failed to add admin (host=%s): %s", host, e.getMessage()));
            }
        }
    }
}

