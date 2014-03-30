package games.pickupbot;

import games.pickupbot.errors.PlayerError;

import java.lang.*;
import java.util.GregorianCalendar;

import org.jaibo.api.helpers.GregorianCalendarHelper;

/**
 * Class describes a person in pickup
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

public class Player implements PickupBotDatabaseManagerListener {
    protected String nick;
    protected String host;
    protected String gameProfile;

    public Player() {
        if (ExtensionObject.Configuration.getBoolean("player.game_profile_required")) {
            ExtensionObject.DatabaseManager.addListener(this);
        }
    }

    public Player(String nick, String host) {
        this();

        this.setNick(nick);
        this.setHost(host);
    }

    public Player(String nick, String host, String gameProfile) {
        this();

        this.setNick(nick);
        this.host = host;
        this.gameProfile = gameProfile;
    }

    public String getGameProfile() {
        return gameProfile;
    }

    public String getNick() {
        return nick;
    }

    public String getFormattedNickName() {
        String playerName;

        if (ExtensionObject.Configuration.getBoolean("player.game_profile_required")) {
            playerName = this.gameProfile;
        } else {
            playerName = this.nick;
        }

        return playerName;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;

        this.checkAndSetGameProfile();
        this.checkLocked();
    }

    public void checkLocked() {
        if (this.gameProfile != null && !this.gameProfile.isEmpty()
                && ExtensionObject.DatabaseManager.isPlayerLocked(this.gameProfile)) {
            GregorianCalendar lockedDateTimeStamp = ExtensionObject.DatabaseManager.getPlayerLockedTime(this.gameProfile);

            if (lockedDateTimeStamp.compareTo(new GregorianCalendar()) <= 0) {
                ExtensionObject.DatabaseManager.removeLockedPlayer(this.gameProfile);
            } else {
                String timeDifference = GregorianCalendarHelper.GetDifferenceAsHumanReadableString(lockedDateTimeStamp,
                        new GregorianCalendar());

                String lockedErrorString = String.format("games.pickupbot.Player is locked [%s remaining]", timeDifference);
                throw new PlayerError(lockedErrorString);
            }
        }
    }

    public void checkAndSetGameProfile() {
        if (this.host != null && !this.host.isEmpty()
                && ExtensionObject.Configuration.getBoolean("player.game_profile_required")) {
            if (ExtensionObject.DatabaseManager.isGameProfileExistsForHost(this.host)) {
                this.gameProfile = ExtensionObject.DatabaseManager.getGameProfileForHost(this.host);
            } else {
                throw new PlayerError("No game profile has been set. Ask admins for the help.");
            }
        }
    }

    // This method will be called before player will be removed from list
    public void beforeRemove() {

    }

    @Override
    public String toString() {
        return String.format("Nick: %s | Host: %s", this.nick, this.host);
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if(obj instanceof Player){
            Player toCompare = (Player) obj;

            return this.getNick() != null && this.getNick().equals(toCompare.getNick())
                    || (this.gameProfile != null && this.gameProfile.equalsIgnoreCase(toCompare.gameProfile));
        }

        return false;
    }

    @Override
    public void playerProfileChanged(String oldProfile, String newProfile) {
        if (this.gameProfile != null && !this.gameProfile.isEmpty() && this.gameProfile.equalsIgnoreCase(oldProfile)) {
            this.gameProfile = newProfile;
        }
    }
}
