package aibo.dataserver;

import aibo.networkconnection.DataServerNetworkConnection;
import aibo.networkconnection.DataServerNetworkConnectionListener;
import aibo.systemextensions.TaskManager;
import org.jaibo.api.dataserver.DataServerInfoPackage;
import org.jaibo.api.dataserver.DataServerInfoStatusCode;
import org.jaibo.api.dataserver.DataServerProcessor;

import java.net.Socket;

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
    private TaskManager taskManager = null;

    private String listenAddress = null;
    private int listenPort;

    public DataServer(String listenAddress, int port, TaskManager taskManager, boolean isDebug) {
        this.listenAddress = listenAddress;
        this.listenPort = port;
        this.taskManager = taskManager;

        this.networkConnection.setDebug(isDebug);
        this.networkConnection.addListener(this);
    }

    @Override
    public DataServerProcessor[] getDataProcessors() {
        return this.taskManager.getDataServerProcessors();
    }

    @Override
    public void run() {
        this.networkConnection.startListenOn(this.listenAddress, this.listenPort);
    }
}