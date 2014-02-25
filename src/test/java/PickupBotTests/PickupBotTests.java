package PickupBotTests;

import AIBO.Extensions.ExtensionMessenger;
import AIBO.Extensions.Games.PickupBot.Game;
import AIBO.Extensions.Games.PickupBot.Object;
import AIBO.Extensions.Games.PickupBot.Player;
import IrcNetwork.DebugIrcMessageSender;

import junit.framework.TestCase;

/**
 * PickupBot tests
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

public final class PickupBotTests extends TestCase {
    private final DebugIrcMessageSender messageSender = new DebugIrcMessageSender();
    private final ExtensionMessenger messenger = new ExtensionMessenger(messageSender, null);

    private final AIBO.Extensions.Games.PickupBot.Object object = new Object();

    private String[] games = {
            "ctf/8",
            "tdm2v2/4",
            "test/2",
            "test2/4"
    };

    private Player testPlayer1 = new Player("testPlayerNick1", "host.org");
    private Player testPlayer2 = new Player("testPlayerNick2", "host.org");


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        object.addGame(Game.tryParse(this.games[3]));

        object.setExtensionMessenger(this.messenger);
    }

    public void testPickupBotAddPlayer() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());

        assertEquals(this.messageSender.isSetTopicEvent(), true);
    }

    public void testPickupBotAddToDefaultGameType() {
        this.object.addPlayer(this.testPlayer1, null);

        assertEquals(this.messageSender.isSetTopicEvent(), true);
    }

    public void testPickupBotGatherPickupGame() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());
        this.object.addPlayer(this.testPlayer2, Game.tryParse(this.games[2]).getGameType());

        assertEquals(this.messageSender.isSetTopicEvent(), true);
        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), true);
        assertEquals(this.messageSender.isSendPrivateMessageEvent(), true);
    }

    public void testPromote() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());
        this.object.promote(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());

        assertEquals(this.messageSender.isSendBroadcastMessageEvent(), true);
    }

    public void testRemovePlayer() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());

        this.messageSender.isSetTopicEvent();


        this.object.removePlayer(this.testPlayer2, Game.tryParse(this.games[2]).getGameType());
        assertEquals(this.messageSender.isSetTopicEvent(), false);

        this.object.removePlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());
        assertEquals(this.messageSender.isSetTopicEvent(), true);
    }

    public void testGetPlayerList() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[3]).getGameType());


        String players = this.object.getPlayers(Game.tryParse(this.games[3]).getGameType(), false);
        assertTrue(players.startsWith(this.testPlayer1.getFormattedNickName()));

        this.object.addPlayer(this.testPlayer2, Game.tryParse(this.games[3]).getGameType());

        players = this.object.getPlayers(Game.tryParse(this.games[3]).getGameType(), false);
        assertEquals(players, this.testPlayer1.getFormattedNickName() + "(0m), " + this.testPlayer2.getFormattedNickName() + "(0m)");
    }
}
