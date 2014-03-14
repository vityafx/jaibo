package aibo.extensions.games.pickupbot;

import aibo.extensions.Extension;
import aibo.extensions.games.pickupbot.commands.eventlisteners.KickPartQuit;
import aibo.extensions.games.pickupbot.commands.eventlisteners.NickChange;
import aibo.extensions.games.pickupbot.commands.messagelisteners.*;
import aibo.extensions.games.pickupbot.errors.GameError;
import aibo.extensions.games.pickupbot.errors.PickupBotError;
import helpers.Configuration;
import helpers.ConfigurationListener;
import ircnetwork.IrcMessageTextModifier;

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

public final class Object extends Extension implements GameListener, ConfigurationListener {
    public final static Configuration Configuration = new Configuration("Games.PickupBot.ini");

    public final static PickupBotDatabaseManager DatabaseManager = new PickupBotDatabaseManager();
    private final ArrayList<Game> games = new ArrayList<Game>();


    public Object() {
        this.setGamesData();
    }

    @Override
    public void run() {

    }

    @Override
    public String getExtensionName() {
        return "games.pickupbot";
    }

    @Override
    public void setCommands() {
        this.addMessageListener(new Add(this));
        this.addMessageListener(new Promote(this));
        this.addMessageListener(new Remove(this));
        this.addMessageListener(new Who(this));
        this.addMessageListener(new Pull(this));
        this.addMessageListener(new Lastgame(this));
        this.addMessageListener(new Reset(this));
        this.addMessageListener(new Iam(this));
        this.addMessageListener(new SetGameProfileBinding(this));
        this.addMessageListener(new RemoveAllGameProfileBindings(this));
        this.addMessageListener(new RemoveGameProfileBinding(this));
        this.addMessageListener(new ChangeGameProfile(this));
        this.addMessageListener(new GetGameProfile(this));
        this.addMessageListener(new Rules(this));
        this.addMessageListener(new Lock(this));
        this.addMessageListener(new Unlock(this));

        this.addEventListener(new KickPartQuit(this));
        this.addEventListener(new NickChange(this));
    }

    @Override
    public String getHelpPage() {
        return Object.Configuration.get("HelpPage");
    }

    @Override
    public void setTopic(String topic) {
        String currentTopic = this.getTopic();

        if (currentTopic == null || !currentTopic.equals(topic)) {
            super.setTopic(topic);
        }
    }

    @Override
    public void configurationChanged() {
        super.configurationChanged();

        this.setGamesData();
    }

    private void setGamesData() {
        this.games.clear();

        this.setGameTypes();
        this.setTournaments();
    }

    private void setGameTypes() {
        String[] gameTypes = Object.Configuration.get("Gametypes").split(" ");

        for (String gameType : gameTypes) {
            Game game = Game.tryParse(gameType);

            this.addGame(game);
        }
    }

    private void setTournaments() {
        String[] tournaments = Object.Configuration.get("Tournaments").split(" ");

        for (String tournamentName : tournaments) {
            Tournament tournament = Tournament.tryParse(tournamentName);

            this.addTournament(tournament);
        }
    }

    public void addGame(Game game) {
        if (game != null) {
            game.addListener(this);

            this.games.add(game);
        }
    }

    public void addTournament(Tournament tournament) {
        if (tournament != null) {
            this.games.add(tournament);
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

    public void clearGames() {
        this.games.clear();
    }

    public void removePlayer(Player player, String gameType) {
        if (player != null) {
            Game game = this.getGameByType(gameType);

            game.removePlayer(player);

            this.updateTopic();
        }
    }

    public void removePlayerFromEachGameType(Player player, boolean updateTopic) {
        for (Game game : this.games) {
            if (!(game instanceof Tournament)) {
                game.removePlayer(player);
            }
        }

        if (updateTopic) {
            this.updateTopic();
        }
    }

    public String getPlayers(String gameType, boolean usingZeroWidthSpace) {
        Game game = this.getGameByType(gameType);

        return game.getPlayerNicknamesAsString(", ", usingZeroWidthSpace, true);
    }

    public String getRegisteredPlayers(String gameType) {
        StringBuilder registeredPlayersListBuilder = new StringBuilder();

        String playersList = this.getPlayers(gameType, true);

        if (playersList.equals("")) {
            playersList = "No players";
        }

        Game game = this.getGameByType(gameType);

        registeredPlayersListBuilder.append(String.format(
                "[%s %d/%d] %s",
                IrcMessageTextModifier.makeBold(game.getGameType()),
                game.addedPlayersCount(),
                game.getMaxPlayers(),
                playersList));

        return registeredPlayersListBuilder.toString();
    }

    public String getGameProfilesAndNicksMapString(String gameType) {
        Game game = this.getGameByType(gameType);

        return game.getGameProfilesAndNicksMapString(", ");
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

            throw new PickupBotError(String.format("Unknown game type %s",
                    IrcMessageTextModifier.makeBold(gameType)));
        }
    }

    public void promote(Player player, String gameType) {
        if (player != null) {
            Game game = this.getGameByType(gameType);

            if (game.isPlayerAdded(player)) {
                this.getExtensionMessenger().sendBroadcastMessage(this.getChannels(), game.getPromoteMessage());
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

    public void substitutePlayer(Player oldPlayer, Player newPlayer) {
        for (Game game : this.games) {
            game.substitutePlayers(oldPlayer, newPlayer);
        }
    }

    public void handleNickChanged(String oldNick, String newNick) {
        for (Game game : this.games) {
            game.handleNickChanged(oldNick, newNick);
        }
    }

    public String lastGame(String gameType) {
        Game game = this.getGameByType(gameType);

        return game.lastGame();
    }

    @Override
    public void pickupFormed(Game game) {
        String notifyPattern = "%s pickup game is ready to play! Players are [%s]";

        String players;

        if (Object.Configuration.getBoolean("player.game_profile_required")) {
            players = game.getGameProfilesAndNicksMapString(", ");
        } else {
            players = game.getPlayerNicknamesAsString(", ", false, false);
        }

        this.getExtensionMessenger().sendBroadcastMessage(this.getChannels(),
                String.format(
                        notifyPattern,
                        IrcMessageTextModifier.makeBold(game.getGameType()),
                        players));

        for (String playerNickName : game.getPlayerNicknames()) {
            this.getExtensionMessenger().sendPrivateMessage(playerNickName,
                    String.format("Your %s pickup game was started! More info on the channel.",
                            IrcMessageTextModifier.makeBold(game.getGameType())));
        }

        ArrayList<Player> playerList = new ArrayList<Player>(game.getPlayerList());

        for (Player player : playerList) {
            this.removePlayerFromEachGameType(player, false);
        }

        this.updateTopic();
    }

    public void reset() {
        for (Game game : this.games) {
            game.clearPlayerList();
        }

        this.updateTopic();
    }

    @Override
    public void playerAutomaticallyRemoved(Player player, Game game) {
        String messagePattern = "%s was automatically removed from %s game type [maximum added time reached]";
        String message = String.format(
                messagePattern,
                IrcMessageTextModifier.makeBold(player.getNick()),
                IrcMessageTextModifier.makeBold(game.getGameType()));

        this.getExtensionMessenger().sendBroadcastMessage(this.getChannels(), message);

        this.getExtensionMessenger().sendPrivateMessage(player.getNick(), message);

        this.updateTopic();
    }
}
