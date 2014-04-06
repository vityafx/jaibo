package games.pickupbot.dataserverprocessors;

import games.pickupbot.ExtensionObject;
import games.pickupbot.Game;
import games.pickupbot.Player;
import games.pickupbot.Tournament;
import games.pickupbot.errors.PickupBotError;

import org.jaibo.api.dataserver.DataServerInfoObject;
import org.jaibo.api.dataserver.DataServerInfoPackage;
import org.jaibo.api.dataserver.DataServerInfoStatusCode;
import org.jaibo.api.dataserver.DataServerProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Processor gives a player list registered in tournament
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

public final class GamesPlayerListProcessor extends DataServerProcessor {
    private ExtensionObject extensionObject = null;

    public GamesPlayerListProcessor(ExtensionObject object) {
        this.setInfoPath("/pickupbot/games");

        this.extensionObject = object;
    }

    @Override
    protected String action() {
        DataServerInfoPackage infoPackage = new DataServerInfoPackage();
        DataServerInfoObject infoObject = new DataServerInfoObject();

        try {
            ArrayList<DataServerInfoObject> games = new ArrayList<DataServerInfoObject>();

            for (Game game : this.extensionObject.getGames()) {
                boolean isTournament = game instanceof Tournament;
                String gameType = game.getGameType();
                List<Player> registeredPlayers = Arrays.asList(game.getPlayers());
                ArrayList<DataServerInfoObject> players = new ArrayList<DataServerInfoObject>();

                for (Player player : registeredPlayers) {
                    DataServerInfoObject playerObject = new DataServerInfoObject();

                    if (!isTournament) {
                        playerObject.putData("nick", player.getNick());
                    }

                    playerObject.putData("game_profile", player.getGameProfile());

                    players.add(playerObject);
                }

                DataServerInfoObject gameObject = new DataServerInfoObject();
                gameObject.putData("is_tournament", isTournament);
                gameObject.putData("game_type", gameType);
                gameObject.putArrayWithoutEscaping("players", players);
                games.add(gameObject);
            }

            infoObject.putArrayWithoutEscaping("games", games);
        } catch (PickupBotError e) {
            infoPackage.setStatus(DataServerInfoStatusCode.ARGUMENT_ERROR);
            infoPackage.setStatusMessage(String.format(e.getMessage()));
        }

        infoPackage.setInfoObject(infoObject);

        return infoPackage.toString();
    }
}