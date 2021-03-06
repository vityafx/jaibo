package games.pickupbot;

import games.pickupbot.errors.GameError;
import org.jaibo.api.IrcMessageTextModifier;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class describes a tournament game
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

public final class Tournament extends Game {

    private Tournament() {

    }

    public Tournament(String tournamentName, int maxPlayers) {
        this.setGameType(tournamentName);
        this.setMaxPlayers(maxPlayers);
    }

    @Override
    public int addedPlayersCount() {
        return ExtensionObject.DatabaseManager.getPlayersRegisteredInTournament(this.getGameType()).length;
    }

    @Override
    public void addPlayer(Player player) {
        if (!ExtensionObject.DatabaseManager.isPlayerRegisteredInTournament(this.getGameType(),
                player.getGameProfile())) {

            ExtensionObject.DatabaseManager.addPlayerInTournament(this.getGameType(), player.getGameProfile());
        } else {
            throw new GameError(String.format("You have been already added to play %s tournament",
                    IrcMessageTextModifier.makeBold(this.getGameType())));
        }
    }

    @Override
    public void removePlayer(Player player) {
        if (ExtensionObject.DatabaseManager.isPlayerRegisteredInTournament(this.getGameType(),
                player.getGameProfile())) {
            ExtensionObject.DatabaseManager.removePlayerFromTournament(this.getGameType(), player.getGameProfile());
        }
    }

    @Override
    public String getRegisteredPlayers() {
        if (ExtensionObject.Configuration.getBoolean("tournaments.players_export")) {
            return this.getLinkToPlayersList();
        } else {
            return this.getSimplePlayersList();
        }
    }

    @Override
    public Player[] getPlayers() {
        ArrayList<Player> playersList = new ArrayList<Player>();
        Player[] players = new Player[]{};

        String[] gameProfiles = ExtensionObject.DatabaseManager.getPlayersRegisteredInTournament(this.getGameType());

        for (String gameProfile : gameProfiles) {
            playersList.add(new Player(null, null, gameProfile));
        }

        return playersList.toArray(players);
    }

    private String getLinkToPlayersList() {
        return ExtensionObject.Configuration.get("tournaments.web_page");
    }

    private String getSimplePlayersList() {
        String[] playersList = ExtensionObject.DatabaseManager.getPlayersRegisteredInTournament(this.getGameType());
        StringBuilder playersStringListBuilder = new StringBuilder();

        if (playersList != null) {
            for (String playerName : playersList) {
                StringBuilder safePlayerNameBuilder = new StringBuilder(playerName);
                safePlayerNameBuilder.insert(1, "\u200B");

                playersStringListBuilder.append(safePlayerNameBuilder.toString());
            }
        }

        return playersStringListBuilder.toString();
    }

    @Override
    public String getPromoteMessage() {
        return String.format("%s more players needed in \"%s\" tournament! - type '!add %s' now!",
                IrcMessageTextModifier.makeBold(String.format("%d", this.remainingPlayersCount())),
                IrcMessageTextModifier.makeBold(this.getGameType()),
                IrcMessageTextModifier.makeBold(this.getGameType()));
    }

    public static Tournament tryParse(String tournamentString) {
        Tournament tournament = null;

        Pattern p = Pattern.compile("^(.*)/([\\d]*)$");

        CharSequence sequence = tournamentString.subSequence(0, tournamentString.length());
        Matcher matcher = p.matcher(sequence);

        if (matcher.matches()) {
            tournament = new Tournament(matcher.group(1), Integer.parseInt(matcher.group(2)));
        }

        return tournament;
    }
}
