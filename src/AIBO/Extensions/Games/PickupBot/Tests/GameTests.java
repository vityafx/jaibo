package AIBO.Extensions.Games.PickupBot.Tests;

import AIBO.Extensions.Games.PickupBot.Errors.GameError;
import AIBO.Extensions.Games.PickupBot.Game;
import AIBO.Extensions.Games.PickupBot.GameListener;
import AIBO.Extensions.Games.PickupBot.Player;
import junit.framework.TestCase;

/**
 * Game class tests
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

    private Player testPlayer1 = new Player("testPlayerNick1", "host.org");
    private Player testPlayer2 = new Player("testPlayerNick2", "host.org");

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
                assertEquals(game.getPlayerNicknames()[0], "testPlayerNick1");
                assertEquals(game.getPlayerNicknames()[1], "testPlayerNick2");
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

        String nickNames = game.getPlayerNicknamesAsString(", ", false);

        assertEquals(nickNames,
                    String.format("%s, %s",
                            this.testPlayer1.getNick(),
                            this.testPlayer2.getNick()));
    }
}
