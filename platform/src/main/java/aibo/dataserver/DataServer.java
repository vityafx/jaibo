package aibo.dataserver;

import aibo.ExtensionManager;
import aibo.networkconnection.DataServerNetworkConnection;
import aibo.networkconnection.DataServerNetworkConnectionListener;
import aibo.systemextensions.TaskManager;
import org.jaibo.api.dataserver.DataServerInfoPackage;
import org.jaibo.api.dataserver.DataServerInfoProvider;
import org.jaibo.api.dataserver.DataServerInfoStatusCode;
import org.jaibo.api.dataserver.status.DataServerInfoArgumentErrorStatus;
import org.jaibo.api.dataserver.status.DataServerInfoIncorrectPathStatus;

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
    public void dataServerRequestReceived(Socket clientSocket, String infoPath) {
        new Thread(new DataServerRequestProcessor(taskManager, clientSocket, networkConnection, infoPath)).start();
    }

    @Override
    public void run() {
        this.networkConnection.startListenOn(this.listenAddress, this.listenPort);
    }
}


class DataServerRequestProcessor implements Runnable {
    private DataServerNetworkConnection connection = null;
    private Socket clientSocket = null;
    private TaskManager taskManager = null;
    private String infoPath = null;


    public DataServerRequestProcessor(TaskManager taskManager, Socket clientSocket,
                                      DataServerNetworkConnection connection, String infoPath) {
        this.taskManager = taskManager;
        this.clientSocket = clientSocket;
        this.connection = connection;
        this.infoPath = infoPath;
    }

    private void processRequest() {
        DataServerInfoProvider[] infoProviders = this.taskManager.getInfoProviders();
        boolean answered = false;

        for (DataServerInfoProvider infoProvider : infoProviders) {
            String answer = infoProvider.checkAndGetInfo(this.infoPath);

            if (answer != null && !answer.isEmpty()) {
                this.send(answer);

                answered = true;
            }
        }

        if (!answered) {
            this.sendError();
        }
    }

    public void sendError() {
        DataServerInfoPackage errorPackage = new DataServerInfoPackage();
        errorPackage.setStatus(DataServerInfoStatusCode.INCORRECT_PATH);

        this.send(errorPackage.toString());
    }

    public void send(String jsonAnswer) {
        if (this.clientSocket != null && jsonAnswer != null && !jsonAnswer.isEmpty()) {
            this.connection.send(this.clientSocket, jsonAnswer);
        }
    }

    @Override
    public void run() {
        this.processRequest();
    }
}