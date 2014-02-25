package AIBO.Extensions.Games.PickupBot;

import java.lang.*;
import java.lang.Object;

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

public class Player {
    protected String nick;
    protected String host;

    public Player() {

    }

    public Player(String nick, String host) {
        this.setNick(nick);
        this.setHost(host);
    }

    public String getNick() {
        return nick;
    }

    public String getFormattedNickName() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return String.format("Nick: %s | Host: %s", this.nick, this.host);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Player){
            Player toCompare = (Player) obj;

            return this.getNick().equalsIgnoreCase(toCompare.getNick());
        }

        return false;
    }
}
