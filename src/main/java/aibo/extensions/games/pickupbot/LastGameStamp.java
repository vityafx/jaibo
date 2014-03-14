package aibo.extensions.games.pickupbot;

import helpers.GregorianCalendarDifference;
import ircnetwork.IrcMessageTextModifier;

import java.util.GregorianCalendar;

/**
 * Last game stamp
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

public final class LastGameStamp {
    private String gameType;
    private String playersList;
    protected GregorianCalendar lastGameDate;

    public LastGameStamp(Game game) {
        this.playersList = game.getFormattedPlayersString(", ");

        this.gameType = game.getGameType();

        this.lastGameDate = new GregorianCalendar();
    }

    public String getStamp() {
        return String.format("Last %s game was played [%s]. Players: [%s]",
                IrcMessageTextModifier.makeBold(this.gameType),
                this.getTimeDifferenceFromLastGame(),
                playersList);
    }

    private String getTimeDifferenceFromLastGame() {
        String difference = GregorianCalendarDifference.GetDifferenceAsHumanReadableString(new GregorianCalendar(), this.lastGameDate);

        if (difference.length() > 0) {
            difference += "ago";
        } else {
            difference = "Just a moment ago";
        }

        return difference;
    }
}