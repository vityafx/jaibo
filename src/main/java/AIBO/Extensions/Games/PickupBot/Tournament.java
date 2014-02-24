package AIBO.Extensions.Games.PickupBot;

import AIBO.Extensions.Games.PickupBot.Errors.GameError;

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
    public void addPlayer(Player player) {
        if (!this.isPlayerAdded(player)) {
            this.checkOverflow();

            this.playerList.add(player);
        } else {
            throw new GameError(String.format("You have been already added to play \"%s\" tournament",
                    this.getGameType()));
        }
    }

    private void checkOverflow() {
        if (remainingPlayersCount() == 0) {
            throw new GameError(String.format("Maximum players registered to play \"%s\" tournament ",
                    this.getGameType()));
        }
    }

    @Override
    public String getPromoteMessage() {
        return String.format("%d more players needed in \"%s\" tournament! - type '!add %s' now!",
                this.remainingPlayersCount(), this.getGameType(), this.getGameType());
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
