package org.jaibo.api.database;

import org.jaibo.api.errors.DatabaseProviderError;

import java.util.ArrayList;

/**
 * Database provider object factory
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

public class DatabaseProviderFactory {
    private static AbstractDatabaseProvider[] providers = new AbstractDatabaseProvider[] {
            new SQLiteProvider(),
            new MySQLProvider()
    };

    public static AbstractDatabaseProvider createDatabaseProvider(String providerName) {
        for(AbstractDatabaseProvider provider : providers) {
            if (provider.getProviderName().equalsIgnoreCase(providerName)) {
                return provider;
            }
        }

        throw new DatabaseProviderError("No such provider");
    }

    public static void setCredentials(DatabaseCredentials credentials) {
        for (AbstractDatabaseProvider provider : providers) {
            provider.setDatabaseCredentials(credentials);
        }
    }
}
