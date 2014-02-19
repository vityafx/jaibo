package AIBO.Extensions.Games.PickupBot.Tests;

import AIBO.Extensions.ExtensionMessenger;
import AIBO.Extensions.Games.PickupBot.*;
import IrcNetwork.DebugIrcMessageSender;
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

    private String testPlayer1Message = ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!add test";
    private String testPlayer2Message = ":testNickName2!testUser2@test2.users.quakenet.org PRIVMSG #test-channel :!add test";
    private String testPlayer3Message = ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!add";

    private String testWrongAddMessage =    ":testNickName1!testUser1@test1.users.quakenet.org " +
                                            "PRIVMSG #test-channel :!add 5492dsgw53";


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        object.addGame(Game.tryParse(this.games[3]));

        object.setExtensionMessenger(this.messenger);
    }

    public void testPickupBotAddPlayer() {
        this.object.processTask(IrcMessage.tryParse(testPlayer1Message));
        this.object.processTask(IrcMessage.tryParse(testPlayer1Message));

        assertEquals(this.messageSender.isSetTopicEvent(), true);

        this.object.processTask(IrcMessage.tryParse(this.testWrongAddMessage));
        assertEquals(this.messageSender.isSendNoticeEvent(), true);
        assertEquals(this.messageSender.isSetTopicEvent(), false);
    }

    public void testPickupBotAddToDefaultGameType() {
        this.object.processTask(IrcMessage.tryParse(this.testPlayer3Message));

        assertEquals(this.messageSender.isSetTopicEvent(), true);
    }

    public void testPickupBotGatherPickupGame() {
        this.object.processTask(IrcMessage.tryParse(this.testPlayer1Message));
        this.object.processTask(IrcMessage.tryParse(this.testPlayer2Message));

        assertEquals(this.messageSender.isSetTopicEvent(), true);
        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), true);
        assertEquals(this.messageSender.isSendPrivateMessageEvent(), true);
    }

    /*
    public void testPromote() {
        this.object.addPlayer(this.testPlayer1Message, Game.tryParse(this.games[2]).getGameType());
        this.object.promote(Game.tryParse(this.games[2]).getGameType());

        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), true);
    }

    public void testRemovePlayer() {
        this.object.addPlayer(this.testPlayer1Message, Game.tryParse(this.games[2]).getGameType());

        this.messageSender.isSetTopicEvent();


        this.object.removePlayer(this.testPlayer3Message, Game.tryParse(this.games[2]).getGameType());
        assertEquals(this.messageSender.isSetTopicEvent(), false);

        this.object.removePlayer(this.testPlayer1Message, Game.tryParse(this.games[2]).getGameType());
        assertEquals(this.messageSender.isSetTopicEvent(), true);
    }

    public void testGetPlayerList() {
        this.object.addPlayer(this.testPlayer1Message, Game.tryParse(this.games[3]).getGameType());


        String players = this.object.getPlayers(Game.tryParse(this.games[3]).getGameType());
        assertEquals(players, this.testPlayer1Message.getNick());

        this.object.addPlayer(this.testPlayer3Message, Game.tryParse(this.games[3]).getGameType());

        players = this.object.getPlayers(Game.tryParse(this.games[3]).getGameType());
        assertEquals(players, this.testPlayer1Message.getNick() + ", " + this.testPlayer3Message.getNick());
    }
    */
}
