package AIBO.Extensions.Games.PickupBot;

import AIBO.Extensions.Games.PickupBot.Errors.GameError;

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

public final class Game {
    private String gameType;
    private int maxPlayers;
    private final ArrayList<Player> playerList = new ArrayList<Player>();

    private final ArrayList<GameListener> listeners = new ArrayList<GameListener>();


    private Game() {

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

    public boolean hasPlayers() {
        return this.addedPlayersCount() > 0;
    }

    public int addedPlayersCount() {
        return this.playerList.size();
    }

    public void addPlayer(Player player) {
        if (!this.isPlayerAdded(player)) {
            this.playerList.add(player);

            this.checkPickupFormed();
        } else {
            throw new GameError(String.format("You have been already added to play %s game type", this.getGameType()));
        }
    }

    public void removePlayer(Player player) {
        if (this.isPlayerAdded(player)) {
            this.playerList.remove(player);
        }
    }

    private void checkPickupFormed() {
        if (playerList.size() == this.maxPlayers) {
            this.pickupFormed();
        }
    }

    private void pickupFormed() {
        this.notifyListeners();

        this.playerList.clear();
    }

    public int remainingPlayersCount() {
        return this.maxPlayers - this.playerList.size();
    }

    public String getPlayerNicknamesAsString(String separator, boolean usingZeroWidthSpace) {
        StringBuilder allAddedPlayerNickNames = new StringBuilder();

        for(Iterator iterator = playerList.iterator(); iterator.hasNext();) {
            Player player = (Player)iterator.next();

            if (!iterator.hasNext())
                separator = "";

            StringBuilder playerNickNameBuilder = new StringBuilder(player.getNick());

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

    private void notifyListeners() {
        for (GameListener listener : this.listeners) {
            listener.pickupFormed(this);
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
}
