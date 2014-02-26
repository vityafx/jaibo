package aibotests;

import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import junit.framework.TestCase;

/**
 * IrcChannel class tests
 * Copyright (C) 2014  Victor Polevoy (vityatheboss@gmail.com)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


public final class IrcChannelTests extends TestCase {
    private String[] messages = {
            ":nick!~user@host.org PRIVMSG #ircbottest :channel message",
            ":nick!~user@host.org PRIVMSG botNickName :private message"
    };

    public void testIrcMessageParser() {
        IrcMessage message1 = IrcMessage.tryParse(this.messages[0]);
        IrcMessage message2 = IrcMessage.tryParse(this.messages[1]);

        assertNotNull(message1);

        assertEquals(message1.getUser(), "nick");
        assertEquals(message1.getHost(), "~user@host.org");
        assertEquals(message1.getMessageType(), IrcMessageType.ChannelMessage);
        assertEquals(message1.getReceiver(), "#ircbottest");
        assertEquals(message1.getMessage(), "channel message");

        assertNotNull(message2);

        assertEquals(message2.getUser(), "nick");
        assertEquals(message2.getHost(), "~user@host.org");
        assertEquals(message2.getMessageType(), IrcMessageType.PrivateMessage);
        assertEquals(message2.getReceiver(), "botNickName");
        assertEquals(message2.getMessage(), "private message");
    }
}
