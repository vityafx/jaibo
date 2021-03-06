import games.pickupbot.ExtensionObject;
import games.pickupbot.Game;
import games.pickupbot.Player;
import junit.framework.TestCase;
import org.jaibo.api.tests.TestExtensionMessenger;
import org.jaibo.api.tests.TestIrcMessageSender;

/**
 * pickupbot tests
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
    private final TestExtensionMessenger messenger = new TestExtensionMessenger();

    private final ExtensionObject object = new ExtensionObject();

    private String[] games = {
            "ctf/8",
            "tdm2v2/4",
            "test/2",
            "test2/4"
    };

    private String testGameAccount1 = "_test_aibo_game_account_1_";
    private String testGameAccount2 = "_test_aibo_game_account_2_";

    private Player testPlayer1 = new Player("testPlayerNick1", "user1@host.org", testGameAccount1);
    private Player testPlayer2 = new Player("testPlayerNick2", "user2@host.org", testGameAccount2);


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.object.clearGames();

        this.object.setTopicAutoUpdate(false);

        for (String gameType : this.games) {
            Game game = Game.tryParse(gameType);

            this.object.addGame(game);
        }

        object.setExtensionMessenger(this.messenger);

        ExtensionObject.DatabaseManager.addGameProfile(this.testPlayer1.getHost(), this.testGameAccount1);
        ExtensionObject.DatabaseManager.addGameProfile(this.testPlayer2.getHost(), this.testGameAccount2);
    }

    public void testPickupBotAddPlayer() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());

        assertEquals(this.messenger.isSetTopicEvent(), true);
    }

    public void testPickupBotAddToDefaultGameType() {
        this.object.addPlayer(this.testPlayer1, null);

        assertEquals(this.messenger.isSetTopicEvent(), true);
    }

    public void testPickupBotGatherPickupGame() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());
        this.object.addPlayer(this.testPlayer2, Game.tryParse(this.games[2]).getGameType());

        assertEquals(this.messenger.isSetTopicEvent(), true);
        assertEquals(this.messenger.isSendBroadcastMessageEvent(), true);
        assertEquals(this.messenger.isSendPrivateMessageEvent(), true);
    }

    public void testPromote() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());
        this.object.promote(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());

        assertEquals(this.messenger.isSendBroadcastMessageEvent(), true);
    }

    public void testRemovePlayer() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());

        this.messenger.isSetTopicEvent();


        this.object.removePlayer(this.testPlayer2, Game.tryParse(this.games[2]).getGameType());
        assertEquals(this.messenger.isSetTopicEvent(), false);

        this.object.removePlayer(this.testPlayer1, Game.tryParse(this.games[2]).getGameType());
        assertEquals(this.messenger.isSetTopicEvent(), true);
    }

    public void testGetPlayerList() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[3]).getGameType());


        String players = this.object.getPlayers(Game.tryParse(this.games[3]).getGameType(), false);
        assertTrue(players.startsWith(this.testPlayer1.getGameProfile()));

        this.object.addPlayer(this.testPlayer2, Game.tryParse(this.games[3]).getGameType());

        players = this.object.getPlayers(Game.tryParse(this.games[3]).getGameType(), false);
        assertEquals(players, this.testPlayer1.getGameProfile() + "(0m), " + this.testPlayer2.getGameProfile() + "(0m)");
    }

    public void testPickupBotReset() {
        this.object.addPlayer(this.testPlayer1, Game.tryParse(this.games[3]).getGameType());

        this.object.reset();


        String players = this.object.getPlayers(Game.tryParse(this.games[3]).getGameType(), false);
        assertEquals(players.length(), 0);
    }
}
