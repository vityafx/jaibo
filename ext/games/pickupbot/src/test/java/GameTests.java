import games.pickupbot.ExtensionObject;
import games.pickupbot.errors.GameError;
import games.pickupbot.Game;
import games.pickupbot.GameListener;
import games.pickupbot.Player;

import junit.framework.TestCase;

/**
 * games.pickupbot.Game class tests
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

public final class GameTests extends TestCase {
    private String[] games = {
        "ctf/8",
        "tdm2v2/4",
        "test/2"
    };

    private String testGameAccount1 = "_test_aibo_game_account_1_";
    private String testGameAccount2 = "_test_aibo_game_account_2_";

    private Player testPlayer1 = new Player("testPlayerNick1", "user1@host.org", testGameAccount1);
    private Player testPlayer2 = new Player("testPlayerNick2", "user2@host.org", testGameAccount2);

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ExtensionObject.DatabaseManager.addGameProfile(testPlayer1.getHost(), testGameAccount1);
        ExtensionObject.DatabaseManager.addGameProfile(testPlayer2.getHost(), testGameAccount2);
    }

    public void testGameParser() {
        Game game = Game.tryParse(this.games[0]);

        assertNotNull(game);

        assertEquals(game.getGameType(), "ctf");
        assertEquals(game.getMaxPlayers(), 8);
        assertNotNull(game.getPlayerList());
        assertEquals(game.getPlayerList().size(), 0);
    }

    public void testAddPlayerTwoTimes() {
        Game game = Game.tryParse(this.games[2]);

        game.addPlayer(this.testPlayer1);

        try {
            game.addPlayer(this.testPlayer1);
        } catch (GameError e) {
            assertTrue(true);
        }

        assertEquals(game.getPlayerList().size(), 1);
    }

    public void testGatherPickupGame() {
        Game game = Game.tryParse(this.games[2]);

        game.addListener(new GameListener() {
            @Override
            public void pickupFormed(Game game) {
                assertTrue(game.getPlayerNicknames()[0].startsWith("testPlayerNick1"));
                assertTrue(game.getPlayerNicknames()[1].startsWith("testPlayerNick2"));
            }

            @Override
            public void playerAutomaticallyRemoved(Player player, Game game) {

            }
        });

        game.addPlayer(this.testPlayer1);
        game.addPlayer(this.testPlayer2);

        assertEquals(game.getPlayerList().size(), 0);
    }

    public void testRemovePlayer() {
        Game game = Game.tryParse(this.games[2]);

        game.addPlayer(this.testPlayer1);

        game.removePlayer(this.testPlayer1);

        assertEquals(game.getPlayerList().size(), 0);
    }

    public void testGetPlayerNickNames() {
        Game game = Game.tryParse(this.games[0]);

        game.addPlayer(this.testPlayer1);
        game.addPlayer(this.testPlayer2);

        String nickNames = game.getPlayerNicknamesAsString(", ", false, false);

        assertEquals(nickNames,
                    String.format("%s, %s",
                            this.testPlayer1.getFormattedNickName(),
                            this.testPlayer2.getFormattedNickName()));
    }

    public void testPlayerSubstitution() {
        Game game = Game.tryParse(this.games[0]);

        game.addPlayer(this.testPlayer1);

        game.substitutePlayers(this.testPlayer1, this.testPlayer2);

        assertTrue(game.hasPlayers());

        assertEquals(game.getPlayerList().get(0), this.testPlayer2);
    }

    public void testLastGame() {
        Game game = Game.tryParse(this.games[2]);

        game.addPlayer(this.testPlayer1);
        game.addPlayer(this.testPlayer2);

        System.out.println(game.lastGame());

        assertTrue(true);
    }
}
