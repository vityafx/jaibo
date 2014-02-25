package PickupBotTests;

import AIBO.Extensions.ExtensionMessenger;
import AIBO.Extensions.Games.PickupBot.*;
import IrcNetwork.DebugIrcMessageSender;
import IrcNetwork.IrcEvent.IrcEvent;
import IrcNetwork.IrcMessage;
import junit.framework.TestCase;

/**
 * Commands test class
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

public final class CommandsTests extends TestCase {
    private final DebugIrcMessageSender messageSender = new DebugIrcMessageSender();
    private final ExtensionMessenger messenger = new ExtensionMessenger(messageSender, null);

    private final AIBO.Extensions.Games.PickupBot.Object object = new AIBO.Extensions.Games.PickupBot.Object();

    private String[] games = {
            "ctf/8",
            "tdm2v2/4",
            "test/2",
            "test2/4"
    };

    private String testAdd1Message =
    ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!add test";

    private String testAdd2Message = ":testNickName2!testUser2@test2.users.quakenet.org PRIVMSG #test-channel :!add test";
    private String testAdd3Message = ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!a";

    private String testWrongAddMessage =    ":testNickName1!testUser1@test1.users.quakenet.org " +
                                            "PRIVMSG #test-channel :!add 5492dsgw53";

    private String testPlayerNickChangeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org NICK :testNickName2";

    private String testPromoteDefaultGameTypeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!p";

    private String testPromoteTestGameTypeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!p test";

    private String testPromoteWrongGameTypeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!p sdafsf3245fgh421";

    private String testRemovePlayerMessageFromDefaultGameType =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!r";

    private String testRemovePlayerMessageFromTestGameType =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!r test";

    private String testWrongRemovePlayerMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!r sdafsf3245fgh421";

    private String testWhoDefaultMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!w";

    private String testWhoTestMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!w";

    private String testWrongWhoMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!w sdafsf3245fgh421";

    private String testPlayer1KickMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org KICK #test-channel testNickName1 :awesome reason";

    private String testPlayer1NickCaseChangeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org NICK :TeStNIcKNAME1";


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.object.clearGames();

        for (String gameType : this.games) {
            Game game = Game.tryParse(gameType);

            this.object.addGame(game);
        }

        object.setExtensionMessenger(this.messenger);
    }

    public void testPickupBotAddPlayer() {
        this.object.processTask(IrcMessage.tryParse(testAdd1Message));
        this.object.processTask(IrcMessage.tryParse(testAdd1Message));

        assertEquals(this.messageSender.isSetTopicEvent(), true);

        this.object.processTask(IrcMessage.tryParse(this.testWrongAddMessage));
        assertEquals(this.messageSender.isSendNoticeEvent(), true);
        assertEquals(this.messageSender.isSetTopicEvent(), false);
    }

    public void testPickupBotAddToDefaultGameType() {
        this.object.processTask(IrcMessage.tryParse(this.testAdd3Message));

        assertEquals(this.messageSender.isSetTopicEvent(), true);
    }

    public void testPickupBotGatherPickupGame() {
        this.object.processTask(IrcMessage.tryParse(this.testAdd1Message));
        this.object.processTask(IrcMessage.tryParse(this.testAdd2Message));

        assertEquals(this.messageSender.isSetTopicEvent(), true);
        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), true);
        assertEquals(this.messageSender.isSendPrivateMessageEvent(), true);
    }

    public void testPromote() {
        this.object.processTask(IrcMessage.tryParse(testAdd3Message));
        this.messageSender.isSendBroadcastMessageEvent();
        this.object.processTask(IrcMessage.tryParse(this.testPromoteDefaultGameTypeMessage));

        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), true);


        this.object.processTask(IrcMessage.tryParse(testAdd1Message));
        this.messageSender.isSendBroadcastMessageEvent();
        this.object.processTask(IrcMessage.tryParse(this.testPromoteTestGameTypeMessage));

        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), true);

        this.object.processTask(IrcMessage.tryParse(this.testPromoteWrongGameTypeMessage));
        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), false);
        assertEquals(this.messageSender.isSendNoticeEvent(), true);
    }

    public void testRemovePlayer() {
        this.object.processTask(IrcMessage.tryParse(testAdd3Message));

        this.messageSender.isSetTopicEvent();


        this.object.processTask(IrcMessage.tryParse(testRemovePlayerMessageFromDefaultGameType));
        assertEquals(this.messageSender.isSetTopicEvent(), true);

        this.object.processTask(IrcMessage.tryParse(testRemovePlayerMessageFromDefaultGameType));
        assertEquals(this.messageSender.isSetTopicEvent(), false);
    }

    public void testGetPlayerList() {
        this.object.processTask(IrcMessage.tryParse(testAdd1Message));
        this.object.processTask(IrcMessage.tryParse(testAdd3Message));

        assertEquals(this.messageSender.isSetTopicEvent(), true);

        this.object.processTask(IrcMessage.tryParse(testWhoDefaultMessage));
        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), true);

        this.object.processTask(IrcMessage.tryParse(testWhoTestMessage));
        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), true);

        this.object.processTask(IrcMessage.tryParse(testWrongWhoMessage));
        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), false);
        assertEquals(this.messageSender.isSendNoticeEvent(), true);
    }

    public void testPlayerRemoveOnKickPartQuit() {
        this.object.processTask(IrcMessage.tryParse(testAdd1Message));
        assertEquals(this.messageSender.isSetTopicEvent(), true);

        this.object.processTask(IrcEvent.tryParse(testPlayer1KickMessage));
        assertEquals(this.messageSender.isSetTopicEvent(), true);
    }

    public void testPlayerNickChange() {
        this.object.processTask(IrcMessage.tryParse(testAdd3Message));
        assertEquals(this.messageSender.isSetTopicEvent(), true);

        this.object.processTask(IrcEvent.tryParse(testPlayerNickChangeMessage));
        assertEquals(this.messageSender.isSetTopicEvent(), false);
        assertTrue(this.object.getPlayers(null, false).startsWith("testNickName2"));
    }

    public void testPlayerCaseNickChange() {
        this.object.processTask(IrcMessage.tryParse(testAdd3Message));
        assertEquals(this.messageSender.isSetTopicEvent(), true);

        this.object.processTask(IrcEvent.tryParse(testPlayer1NickCaseChangeMessage));
        assertEquals(this.messageSender.isSetTopicEvent(), false);
        assertTrue(this.object.getPlayers(null, false).startsWith("TeStNIcKNAME1"));
    }
}
