package aibotests;

import ircnetwork.IrcUser;

import junit.framework.TestCase;

/**
 * IrcUser class tests
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

public final class IrcUserTests extends TestCase {
    private String[] users = {
            ":nick!~user@host.org",
            ":esdf!esdf@esdf.users.quakenet.org",
            ":ph0en|X!~phoenix@vlhurgs.net"
    };

    public void testIrcMessageParser() {
        IrcUser user1 = IrcUser.tryParse(this.users[0]);
        IrcUser user2 = IrcUser.tryParse(this.users[1]);
        IrcUser user3 = IrcUser.tryParse(this.users[2]);

        assertNotNull(user1);

        assertEquals(user1.getNick(), "nick");
        assertEquals(user1.getName(), "~user");
        assertEquals(user1.getHost(), "host.org");


        assertNotNull(user2);

        assertEquals(user2.getNick(), "esdf");
        assertEquals(user2.getName(), "esdf");
        assertEquals(user2.getHost(), "esdf.users.quakenet.org");


        assertNotNull(user3);

        assertEquals(user3.getNick(), "ph0en|X");
        assertEquals(user3.getName(), "~phoenix");
        assertEquals(user3.getHost(), "vlhurgs.net");
    }
}
