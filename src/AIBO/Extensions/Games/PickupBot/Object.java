package AIBO.Extensions.Games.PickupBot;

import AIBO.Extensions.Extension;
import AIBO.Extensions.Games.PickupBot.Commands.EventListeners.KickPartQuit;
import AIBO.Extensions.Games.PickupBot.Commands.MessageListeners.Add;
import AIBO.Extensions.Games.PickupBot.Commands.MessageListeners.Promote;
import AIBO.Extensions.Games.PickupBot.Commands.MessageListeners.Remove;
import AIBO.Extensions.Games.PickupBot.Commands.MessageListeners.Who;
import AIBO.Extensions.Games.PickupBot.Errors.GameError;
import AIBO.Extensions.Games.PickupBot.Errors.PickupBotError;
import Helpers.Configuration;

import java.util.ArrayList;

/**
 * PickupBot gathers people to play games
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

public final class Object extends Extension implements GameListener {
    private String defaultGameType;

    private final ArrayList<Game> games = new ArrayList<Game>();

    public final static Configuration Configuration = new Configuration("AIBO/Extensions/Games/PickupBot/settings.ini");


    public Object() {
        this.setGameTypes();
    }

    @Override
    public void run() {

    }

    @Override
    public String getExtensionName() {
        return "Games.PickupBot";
    }

    @Override
    public void setCommands() {
        this.addMessageListener(new Add(this));
        this.addMessageListener(new Promote(this));
        this.addMessageListener(new Remove(this));
        this.addMessageListener(new Who(this));

        this.addEventListener(new KickPartQuit(this));
    }

    @Override
    public void setTopic(String topic) {
        String currentTopic = this.getTopic();

        if (currentTopic == null || !currentTopic.equals(topic)) {
            super.setTopic(topic);
        }
    }

    private void setGameTypes() {
        String[] gameTypes = Object.Configuration.getConfigurationHashMap().get("Gametypes").split(" ");

        for (String gameType : gameTypes) {
            Game game = Game.tryParse(gameType);

            this.addGame(game);
        }

        Game defaultGameType = this.games.get(0);

        this.setDefaultGameType(defaultGameType.getGameType());
    }

    public void addGame(Game game) {
        if (game != null) {
            game.addListener(this);

            this.games.add(game);
        }
    }

    public void addPlayer(Player player, String gameType) {
        if (player != null) {
            Game game = this.getGameByType(gameType);

            try {
                game.addPlayer(player);

                this.updateTopic();
            } catch (GameError e) {
                this.getExtensionMessenger().sendNotice(player.getNick(), e.getMessage());
            }
        }
    }

    public void removePlayer(Player player, String gameType) {
        if (player != null) {
            Game game = this.getGameByType(gameType);

            game.removePlayer(player);

            this.updateTopic();
        }
    }

    public void removePlayerFromEachGameType(Player player) {
        for (Game game : this.games) {
            game.removePlayer(player);
        }

        this.updateTopic();
    }

    public String getPlayers(String gameType, boolean usingZeroWidthSpace) {
        Game game = this.getGameByType(gameType);

        return game.getPlayerNicknamesAsString(", ", usingZeroWidthSpace);
    }

    public String getRegisteredPlayers(String gameType) {
        StringBuilder registeredPlayersListBuilder = new StringBuilder();

        String playersList = this.getPlayers(gameType, true);

        if (playersList.equals("")) {
            playersList = "No players";
        }

        Game game = this.getGameByType(gameType);

        registeredPlayersListBuilder.append(String.format("[%s] %s", game.getGameType(), playersList));

        return registeredPlayersListBuilder.toString();
    }

    private Game getGameByType(String gameType) {
        if (gameType == null) {
            return this.games.get(0);
        } else {
            for (Game game : this.games) {
                if (game.getGameType().equalsIgnoreCase(gameType)) {
                    return game;
                }
            }

            throw new PickupBotError(String.format("Unknown game type %s", gameType));
        }
    }

    public void promote(Player player, String gameType) {
        if (player != null) {
            Game game = this.getGameByType(gameType);

            if (game.isPlayerAdded(player)) {
                this.getExtensionMessenger().sendBroadcastMessage(this.getChannels(),
                        String.format("%d more players needed in %s game type! - type '!add' now!",
                                game.remainingPlayersCount(), game.getGameType()));
            } else {
                throw new PickupBotError("You cannot promote while not added");
            }
        }
    }

    private void updateTopic() {
        StringBuilder topicBuilder = new StringBuilder();

        for (Game game : this.games) {
            if (game.hasPlayers()) {
                topicBuilder.append(String.format(
                        "%s[%d/%d] ",
                        game.getGameType(),
                        game.addedPlayersCount(),
                        game.getMaxPlayers()));
            }
        }

        this.setTopic(topicBuilder.toString());
    }

    public String getDefaultGameType() {
        return this.defaultGameType;
    }

    private void setDefaultGameType(String gameType) {
        this.defaultGameType = gameType;
    }

    @Override
    public void pickupFormed(Game game) {
        String notifyPattern = "%s pickup game is ready to play! Players are [%s]";

        this.getExtensionMessenger().sendBroadcastMessage(this.getChannels(),
                String.format(notifyPattern, game.getGameType(), game.getPlayerNicknamesAsString(", ", true)));

        for (String playerNickName : game.getPlayerNicknames()) {
            this.getExtensionMessenger().sendPrivateMessage(playerNickName,
                    String.format("Your %s pickup game was started! More info on the channel.", game.getGameType()));
        }
    }
}
