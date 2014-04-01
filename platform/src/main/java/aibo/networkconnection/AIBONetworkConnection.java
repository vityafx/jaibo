package aibo.networkconnection;

import aibo.AIBO;
import org.jaibo.api.NetworkConnectionInterface;
import org.jaibo.api.NetworkConnectionListener;
import org.jaibo.api.helpers.ConfigurationListener;

import java.util.ArrayList;

/**
 * AIBO's network connection
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

public final class AIBONetworkConnection implements ConfigurationListener, NetworkConnectionListener,
        NetworkConnectionInterface {
    private final NetworkConnection networkConnection = new NetworkConnection();
    private ArrayList<AIBONetworkConnectionListener> listeners = new ArrayList<AIBONetworkConnectionListener>();

    private AIBONetworkConnection() {
        AIBO.Configuration.addListener(this);

        configurationChanged();
    }

    public AIBONetworkConnection(String server, int port) {
        this();

        this.networkConnection.setAddress(server);
        this.networkConnection.setPort(port);

        this.networkConnection.addListener(this);
    }

    @Override
    public void configurationChanged() {
        this.networkConnection.setDebug(AIBO.Configuration.getBoolean("Network.debug"));
    }

    public void removeListener(AIBONetworkConnectionListener listener) {
        this.listeners.remove(listener);
    }

    public void addListener(AIBONetworkConnectionListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    private void notifyListeners(String ircData) {
        for (AIBONetworkConnectionListener listener : this.listeners) {
            listener.ircDataReceived(ircData);
        }
    }

    public void connect() {
        this.networkConnection.connect();
    }

    @Override
    public void dataReceived(String data) {
        this.notifyListeners(data.replace("\r\n", ""));
    }

    @Override
    public void send(String data) {
        this.networkConnection.send(data);
    }
}
