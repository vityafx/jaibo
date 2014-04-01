package aibo;

import aibo.dataserver.DataServer;
import aibo.systemextensions.TaskManager;
import org.jaibo.api.database.DatabaseProvider;
import org.jaibo.api.helpers.Configuration;
import org.jaibo.api.IrcEvent;
import org.jaibo.api.IrcMessage;
import aibo.ircnetwork.IrcNetwork;
import org.jaibo.api.IrcNetworkListener;


/**
 * aibo bot realization
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

public final class AIBO implements IrcNetworkListener {
    public static Configuration Configuration = new Configuration("settings.ini");

    private TaskManager taskManager;

    private IrcNetwork ircNetwork;


    public AIBO() {
        DatabaseProvider.setDefaultDatabase(Configuration.get("aibo.database_name"));

        this.ircNetwork = new IrcNetwork(
                Configuration.get("IrcConnection.host").split(" "),
                Integer.parseInt(Configuration.get("IrcConnection.port")), this);

        this.taskManager = new TaskManager(this.ircNetwork.getMessageSender());

        this.setUpDataServer();
    }

    public AIBO(String... extensionNames) {
        this();

        this.taskManager.getExtensionManager().addExtensionsByNames(extensionNames);
    }

    @Override
    public void ircMessageReceived(String message) {
        if (message != null) {
            IrcMessage ircMessage = IrcMessage.tryParse(message);
            IrcEvent ircEvent = IrcEvent.tryParse(message);

            if (ircMessage != null) {
                this.taskManager.notifyMessageListeners(ircMessage);
            } else if (ircEvent != null) {
                this.taskManager.notifyEventListeners(ircEvent);
            } else {
                this.taskManager.notifyServerListeners(message);
            }
        }
    }

    public void run() {
        this.ircNetwork.connect();
    }

    public void setUpDataServer() {
        if (Configuration.getBoolean("data_server")) {
            try {
                int listenPort = Integer.parseInt(Configuration.get("data_server.listen_port"));
                String listenAddress = Configuration.get("data_server.listen_ip");
                boolean isDebug = Configuration.getBoolean("network.debug");

                new Thread(new DataServer(listenAddress, listenPort, this.taskManager, isDebug)).start();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
