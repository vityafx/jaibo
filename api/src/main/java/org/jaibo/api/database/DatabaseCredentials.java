package org.jaibo.api.database;

/**
 * Global database credentials
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

public final class DatabaseCredentials {
    private String host;        // for example: localhost:3306 (with port) or simply localhost (without port)
    private String username;
    private String password;
    private String databaseName;


    public DatabaseCredentials() {

    }

    public DatabaseCredentials(String host, String username, String password, String databaseName) {
        this.setHost(host);
        this.setUsername(username);
        this.setPassword(password);
        this.setDatabaseName(databaseName);
    }



    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getHost() {
        return host;
    }

    public String getDatabaseName() {
        return databaseName;
    }



    public void setHost(String host) {
        this.host = host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


    public String getMysqlUrlString() {
        return "jdbc:mysql://" + this.getHost() + "/" + this.getDatabaseName();
    }
}
