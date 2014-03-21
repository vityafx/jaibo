package aibo.ircnetwork;

import aibo.networkconnection.NetworkConnection;
import org.jaibo.api.IrcCommandSenderInterface;

/**
 * Realization of simple commands sender to irc network
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

public class IrcCommandSender implements IrcCommandSenderInterface {
    private NetworkConnection connection;


    public IrcCommandSender() {

    }

    public IrcCommandSender(NetworkConnection connection) {
        this.connection = connection;
    }


    @Override
    public void sendIrcCommand(String command, String arguments) {
        if (this.connection != null) {
            connection.send(String.format("%s %s", command, arguments));
        }
    }
}
