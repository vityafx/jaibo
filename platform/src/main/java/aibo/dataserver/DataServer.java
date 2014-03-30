package aibo.dataserver;

import aibo.networkconnection.DataServerNetworkConnection;
import aibo.networkconnection.DataServerNetworkConnectionListener;
import org.jaibo.api.dataserver.DataServerInfoProvider;

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

public final class DataServer extends Thread implements DataServerNetworkConnectionListener {
    private final DataServerNetworkConnection networkConnection = new DataServerNetworkConnection();
    private final static ArrayList<DataServerInfoProvider> InfoProviders = new ArrayList<DataServerInfoProvider>();

    public DataServer(String listenAddress, int port) {
        this.networkConnection.startListenOn(listenAddress, port);
    }

    @Override
    public void dataServerRequestReceived(String data) {
        String infoPath = null;

        // get path from data (parse GET /*)

        for (DataServerInfoProvider infoProvider : InfoProviders) {
            String answer = infoProvider.checkAndExecute(infoPath);

            if (answer != null) {
                this.networkConnection.send(answer);

                break;
            }
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
}
