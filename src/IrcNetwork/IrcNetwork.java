package IrcNetwork;

import NetworkConnection.NetworkConnection;
import NetworkConnection.NetworkConnectionListener;

import java.util.ArrayList;

/**
 * Class introduces an Irc Network
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

public final class IrcNetwork implements NetworkConnectionListener {
    private ArrayList<IrcChannel> channels = new ArrayList<IrcChannel>();

    private IrcNetworkListener ircNetworkListener;

    private IrcMessageSender sender;
    private NetworkConnection connection = NetworkConnection.sharedInstance;


    public IrcNetwork(String server, int port, IrcNetworkListener ircNetworkListener) {
        this.sender = new IrcMessageSender(this.connection);

        this.ircNetworkListener = ircNetworkListener;

        this.connection.addListener(this);

        this.connection.connect(server, port);
    }

    @Override
    public void dataReceived(String data) {
        this.ircNetworkListener.ircMessageReceived(data);
    }
}
