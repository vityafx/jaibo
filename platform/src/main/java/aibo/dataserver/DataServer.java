package aibo.dataserver;

import aibo.networkconnection.DataServerNetworkConnection;
import aibo.networkconnection.DataServerNetworkConnectionListener;

import org.jaibo.api.dataserver.DataServerInfoProvider;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Data server
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

public final class DataServer implements DataServerNetworkConnectionListener, Runnable {
    private final DataServerNetworkConnection networkConnection = new DataServerNetworkConnection();
    private final static ArrayList<DataServerInfoProvider> InfoProviders = new ArrayList<DataServerInfoProvider>();

    private Socket clientSocket = null;
    private String listenAddress = null;
    private int listenPort;

    public DataServer(String listenAddress, int port, boolean isDebug) {
        this.listenAddress = listenAddress;
        this.listenPort = port;

        this.networkConnection.setDebug(isDebug);
    }

    @Override
    public void dataServerRequestReceived(Socket clientSocket, String infoPath) {
        for (DataServerInfoProvider infoProvider : InfoProviders) {
            String answer = infoProvider.checkAndGetInfo(infoPath);

            this.send(answer);
        }
    }

    public static ArrayList<DataServerInfoProvider> getInfoProviders() {
        return InfoProviders;
    }

    public static void addInfoProvider(DataServerInfoProvider provider) {
        if (!InfoProviders.contains(provider)) {
            InfoProviders.add(provider);
        }
    }

    public static void removeInfoProvider(DataServerInfoProvider provider) {
        if (InfoProviders.contains(provider)) {
            InfoProviders.remove(provider);
        }
    }

    public void send(String jsonAnswer) {
        if (this.clientSocket != null && jsonAnswer != null && !jsonAnswer.isEmpty()) {
            this.networkConnection.send(this.clientSocket, jsonAnswer);
        }
    }

    @Override
    public void run() {
        this.networkConnection.startListenOn(this.listenAddress, this.listenPort);
    }
}
