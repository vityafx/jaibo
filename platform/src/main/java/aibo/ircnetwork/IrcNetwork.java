package aibo.ircnetwork;

import aibo.networkconnection.NetworkConnection;
import org.jaibo.api.NetworkConnectionListener;
import org.jaibo.api.IrcChannel;
import org.jaibo.api.IrcNetworkListener;

import java.util.ArrayList;
import java.util.Arrays;

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
    private ArrayList<String> servers = new ArrayList<String>();
    private static int CurrentServerIndex = 0;

    private IrcNetworkListener ircNetworkListener;

    private IrcMessageSender sender;
    private NetworkConnection connection = NetworkConnection.sharedInstance;


    public IrcNetwork(String[] servers, int port, IrcNetworkListener ircNetworkListener) {
        this.setServers(servers);

        this.sender = new IrcMessageSender(this.connection);

        this.ircNetworkListener = ircNetworkListener;

        this.connection.addListener(this);
        this.connection.setAddress(this.getServer());
        this.connection.setPort(port);
    }

    public void connect() {
        this.connection.connect();
    }

    public IrcMessageSender getMessageSender() {
        return this.sender;
    }

    @Override
    public void dataReceived(String data) {
        this.ircNetworkListener.ircMessageReceived(data.replace("\r\n",""));
    }

    private void setServers(String[] servers) {
        this.servers.addAll(Arrays.asList(servers));
    }

    private String getServer() {
        if (IrcNetwork.CurrentServerIndex >= this.servers.size()) {
            IrcNetwork.CurrentServerIndex = 0;
        }

        return this.servers.get(IrcNetwork.CurrentServerIndex++);
    }
}
