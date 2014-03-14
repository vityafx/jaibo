package aibo.extensions.games.pickupbot;

import aibo.extensions.games.pickupbot.errors.GameError;
import ircnetwork.IrcMessageTextModifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class describes a pickup game
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

public class Game {
    protected String gameType;
    protected int maxPlayers;
    protected LastGameStamp lastGameStamp;
    protected int gameId;
    protected final ArrayList<Player> playerList = new ArrayList<Player>();

    private final ArrayList<GameListener> listeners = new ArrayList<GameListener>();


    protected Game() {

    }

    public Game(String gameType, int maxPlayers) {
        this.gameType = gameType;
        this.maxPlayers = maxPlayers;
    }


    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public boolean isPlayerAdded(Player player) {
        return this.playerList.contains(player);
    }

    public boolean isPlayerAddedByHost(Player player) {
        for (Player registeredPlayer : this.playerList) {
            String host = registeredPlayer.getHost();

            if (host != null && host.equalsIgnoreCase(player.getHost())) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPlayers() {
        return this.addedPlayersCount() > 0;
    }

    public int addedPlayersCount() {
        return this.playerList.size();
    }

    public void addPlayer(Player player) {
        if (!this.isPlayerAdded(player)) {
            AutoRemovablePlayer autoRemovablePlayer = new AutoRemovablePlayer(player, this);

            this.playerList.add(autoRemovablePlayer);

            this.checkPickupFormed();
        } else {
            throw new GameError(String.format("You have been already added to play %s game type",
                    IrcMessageTextModifier.makeBold(this.getGameType())));
        }
    }

    public Player getPlayerByNick(String nick) {
        for (Player player : this.playerList) {
            if (player.getNick().equalsIgnoreCase(nick)) {
                return player;
            }
        }

        return null;
    }

    public Player getPlayerByGameProfile(String gameProfile) {
        for (Player player : this.playerList) {
            if (player.getGameProfile().equalsIgnoreCase(gameProfile)) {
                return player;
            }
        }

        return null;
    }

    public void removePlayer(Player player) {
        if (this.isPlayerAdded(player)) {
            if (player.getNick() != null) {
                this.getPlayerByNick(player.getNick()).beforeRemove();
            } else if (player.getGameProfile() != null) {
                    this.getPlayerByGameProfile(player.getGameProfile()).beforeRemove();
            }

            this.playerList.remove(player);
        }
    }

    public void automaticallyRemovePlayer(Player player) {
        this.removePlayer(player);

        this.notifyPlayerAutoRemovedUpListeners(player);
    }

    public void clearPlayerList() {
        this.playerList.clear();
    }

    protected void checkPickupFormed() {
        if (playerList.size() == this.maxPlayers) {
            this.lastGameStamp = new LastGameStamp(this);

            this.pickupFormed();
        }
    }

    protected void pickupFormed() {
        this.notifyPickupFormedUpListeners();

        this.clearPlayerList();
    }

    public String lastGame() {
        if (this.lastGameStamp == null) {
            return "No games was played yet";
        } else {
            return this.lastGameStamp.getStamp();
        }
    }

    public int remainingPlayersCount() {
        return this.maxPlayers - this.playerList.size();
    }

    public String getPromoteMessage() {
        return String.format("%s more players needed in %s game type! - type '!add %s' now!",
                IrcMessageTextModifier.makeBold(String.format("%d", this.remainingPlayersCount())),
                IrcMessageTextModifier.makeBold(this.getGameType()),
                IrcMessageTextModifier.makeBold(this.getGameType()));
    }

    public String getPlayerNicknamesAsString(String separator, boolean usingZeroWidthSpace, boolean formattedNickNames) {
        StringBuilder allAddedPlayerNickNames = new StringBuilder();

        for(Iterator iterator = playerList.iterator(); iterator.hasNext();) {
            Player player = (Player)iterator.next();

            if (!iterator.hasNext())
                separator = "";

            StringBuilder playerNickNameBuilder = new StringBuilder();

            if (formattedNickNames) {
                playerNickNameBuilder.append(player.getFormattedNickName());
            } else {
                playerNickNameBuilder.append(player.getNick());
            }

            if (usingZeroWidthSpace) {
                playerNickNameBuilder.insert(1, "\u200B");
            }

            allAddedPlayerNickNames.append(String.format("%s%s", playerNickNameBuilder.toString(), separator));
        }

        return allAddedPlayerNickNames.toString();
    }

    public String[] getPlayerNicknames() {
        ArrayList<String> allAddedPlayerNickNames = new ArrayList<String>();
        String[] nickNames = new String[]{};

        for (Player player : this.playerList) {
            allAddedPlayerNickNames.add(player.getNick());
        }

        return allAddedPlayerNickNames.toArray(nickNames);
    }

    public String getGameProfilesAsString(String separator) {
        StringBuilder allGameProfiles = new StringBuilder();

        for(Iterator iterator = playerList.iterator(); iterator.hasNext();) {
            Player player = (Player)iterator.next();
            StringBuilder gameProfile = new StringBuilder(player.getGameProfile());
            gameProfile.insert(1, "\u200B");

            if (!iterator.hasNext())
                separator = "";

            allGameProfiles.append(String.format("%s%s", gameProfile, separator));
        }

        return allGameProfiles.toString();
    }

    public String[] getGameProfiles() {
        ArrayList<String> allAddedPlayerNickNames = new ArrayList<String>();
        String[] nickNames = new String[]{};

        for (Player player : this.playerList) {
            allAddedPlayerNickNames.add(player.getGameProfile());
        }

        return allAddedPlayerNickNames.toArray(nickNames);
    }

    public String getGameProfilesAndNicksMapString(String separator) {
        String playersString = "";

        for(int i = 0; i < this.playerList.size(); i++) {
            StringBuilder gameProfile = new StringBuilder(this.playerList.get(i).getGameProfile());
            gameProfile.insert(1, "\u200B");

            playersString += String.format("%s(%s)", this.playerList.get(i).getNick(), gameProfile);

            if (i < this.playerList.size() - 1) {
                playersString += separator;
            }
        }

        return playersString;
    }

    public String getFormattedPlayersString(String separator) {
        if (Object.Configuration.getBoolean("player.game_profile_required")) {
            return this.getGameProfilesAsString(separator);
        } else {
            return this.getPlayerNicknamesAsString(separator, false, false);
        }
    }

    private void notifyPickupFormedUpListeners() {
        for (GameListener listener : this.listeners) {
            listener.pickupFormed(this);
        }
    }

    private void notifyPlayerAutoRemovedUpListeners(Player player) {
        for (GameListener listener : this.listeners) {
            listener.playerAutomaticallyRemoved(player, this);
        }
    }

    public void addListener(GameListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(GameListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getGameId() {
        return this.gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public static Game tryParse(String gameString) {
        Game game = null;

        Pattern p = Pattern.compile("^(.*)/([\\d]*)$");

        CharSequence sequence = gameString.subSequence(0, gameString.length());
        Matcher matcher = p.matcher(sequence);

        if (matcher.matches()) {
            game = new Game(matcher.group(1), Integer.parseInt(matcher.group(2)));
        }

        return game;
    }

    public void substitutePlayers(Player playerToSubstitute, Player newPlayer) {
        if (this.isPlayerAdded(playerToSubstitute)) {
            if (!this.isPlayerAdded(newPlayer)) {
                this.removePlayer(playerToSubstitute);
                this.addPlayer(newPlayer);
            } else {
                throw new GameError(String.format("Player %s is already added in %s",
                        IrcMessageTextModifier.makeBold(newPlayer.getNick()),
                        IrcMessageTextModifier.makeBold(this.getGameType())));
            }
        }
    }

    public void handleNickChanged(String oldNick, String newNick) {
        if (this.isPlayerAdded(new Player(oldNick, null))) {
            if (!this.isPlayerAdded(new Player(newNick, null))) {
                Player player = this.getPlayerByNick(oldNick);
                player.setNick(newNick);
            } else {
                throw new GameError(String.format("Player %s is already added in %s",
                        IrcMessageTextModifier.makeBold(newNick),
                        IrcMessageTextModifier.makeBold(this.getGameType())));
            }
        }
    }

    @Override
    public String toString() {
        return this.gameType;
    }
}
