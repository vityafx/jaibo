import games.pickupbot.PickupBotDatabaseManager;
import junit.framework.TestCase;
import org.jaibo.api.database.DatabaseCredentials;
import org.jaibo.api.database.DatabaseProvider;

import java.util.GregorianCalendar;

/**
 * PickupBot database manager tests
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

public final class DatabaseManagerTests extends TestCase {
    private String gameAccount1 = "vitya";
    private String host1 = "vitya.users.quakenet.org";
    private String host2 = "1.1.1.1";
    private String host3 = "2.12.2.5";

    // To test with mysql simply set correct credentials and change provider in `setUp` method
    private static DatabaseCredentials testCredentials = new DatabaseCredentials(null, null, null, "aibo");

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        DatabaseProvider.setCredentials(testCredentials);
        DatabaseProvider.setDatabaseProvider("sqlite");
    }

    public void testGameProfiles() {
        PickupBotDatabaseManager manager = new PickupBotDatabaseManager();

        manager.addGameProfile(host1, gameAccount1);
        manager.addGameProfile(host2, gameAccount1);

        assertTrue(manager.isGameProfileExistsForHost(host1));
        assertTrue(manager.isGameProfileExistsForHost(host2));
        assertFalse(manager.isGameProfileExistsForHost(host3));

        manager.removeGameProfile(gameAccount1);

        assertFalse(manager.isGameProfileExistsForHost(host1));
        assertFalse(manager.isGameProfileExistsForHost(host2));
        assertFalse(manager.isGameProfileExistsForHost(host3));


        manager.addGameProfile(host1, gameAccount1);
        manager.addGameProfile(host2, gameAccount1);

        assertTrue(manager.isGameProfileExistsForHost(host1));
        assertTrue(manager.isGameProfileExistsForHost(host2));
        assertFalse(manager.isGameProfileExistsForHost(host3));

        manager.removeHostBindingForGameProfile(host1, gameAccount1);
        assertFalse(manager.isGameProfileExistsForHost(host1));

        manager.removeHostBindingForGameProfile(host2, gameAccount1);
        assertFalse(manager.isGameProfileExistsForHost(host2));
    }

    public void testLockedPlayers() {
        PickupBotDatabaseManager manager = new PickupBotDatabaseManager();

        assertFalse(manager.isPlayerLocked(gameAccount1));

        manager.addLockedPlayer(gameAccount1);

        assertTrue(manager.isPlayerLocked(gameAccount1));

        manager.removeLockedPlayer(gameAccount1);

        assertFalse(manager.isPlayerLocked(gameAccount1));


        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.MINUTE, 1);
        manager.addLockedPlayer(gameAccount1, calendar.getTimeInMillis());

        assertTrue(manager.isPlayerLocked(gameAccount1));

        manager.removeLockedPlayer(gameAccount1);

        assertFalse(manager.isPlayerLocked(gameAccount1));

    }
}
