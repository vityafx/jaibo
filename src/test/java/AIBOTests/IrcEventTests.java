package AIBOTests;

import IrcNetwork.IrcEvent.IrcEvent;
import IrcNetwork.IrcEvent.IrcEventType;
import junit.framework.TestCase;

/**
 * Irc events tests
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

public final class IrcEventTests extends TestCase {
    private String nickEvent = ":old_nick!host.org NICK :new_nick";
    private String tabCloseEvent = ":old_nick!host.org PART #test_channel :close message";
    private String quitEvent = ":old_nick!host.org QUIT :quit message";
    private String kickEvent = ":old_nick!host.org KICK #test_channel kicked_user_nick :kick message";
    private String modeEvent = ":old_nick!host.org MODE #test_channel +o user_nick_name";
    private String joinEvent = ":old_nick!host.org JOIN #test_channel";


    public void testNickChangedEvent() {
        IrcEvent event = IrcEvent.tryParse(nickEvent);

        assertNotNull(event);

        assertEquals(event.getUser(), "old_nick");
        assertEquals(event.getHost(), "host.org");
        assertEquals(event.getArgument("NewNickName"), "new_nick");
        assertEquals(event.getEventType(), IrcEventType.Nick);
    }

    public void testTabClosedChangedEvent() {
        IrcEvent event = IrcEvent.tryParse(tabCloseEvent);

        assertNotNull(event);

        assertEquals(event.getUser(), "old_nick");
        assertEquals(event.getHost(), "host.org");
        assertEquals(event.getChannel(), "#test_channel");
        assertEquals(event.getArgument("CloseMessage"), "close message");
        assertEquals(event.getEventType(), IrcEventType.Part);
    }

    public void testQuitChangedEvent() {
        IrcEvent event = IrcEvent.tryParse(quitEvent);

        assertNotNull(event);

        assertEquals(event.getUser(), "old_nick");
        assertEquals(event.getHost(), "host.org");
        assertEquals(event.getArgument("QuitMessage"), "quit message");
        assertEquals(event.getEventType(), IrcEventType.Quit);
    }

    public void testKickChangedEvent() {
        IrcEvent event = IrcEvent.tryParse(kickEvent);

        assertNotNull(event);

        assertEquals(event.getUser(), "old_nick");
        assertEquals(event.getHost(), "host.org");
        assertEquals(event.getChannel(), "#test_channel");
        assertEquals(event.getArgument("UserKicked"), "kicked_user_nick");
        assertEquals(event.getArgument("KickMessage"), "kick message");
        assertEquals(event.getEventType(), IrcEventType.Kick);
    }

    public void testJoinChangedEvent() {
        IrcEvent event = IrcEvent.tryParse(joinEvent);

        assertNotNull(event);

        assertEquals(event.getUser(), "old_nick");
        assertEquals(event.getHost(), "host.org");
        assertEquals(event.getChannel(), "#test_channel");
        assertEquals(event.getEventType(), IrcEventType.Join);
    }

    public void testModeChangedEvent() {
        IrcEvent event = IrcEvent.tryParse(modeEvent);

        assertNotNull(event);

        assertEquals(event.getUser(), "old_nick");
        assertEquals(event.getHost(), "host.org");
        assertEquals(event.getArgument("User"), "user_nick_name");
        assertEquals(event.getEventType(), IrcEventType.Mode);
    }
}
