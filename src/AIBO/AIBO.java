package AIBO;

import AIBO.Extensions.TaskManager;
import Helpers.Configuration;
import IrcNetwork.IrcNetwork;
import IrcNetwork.IrcNetworkListener;
import IrcNetwork.IrcMessage;
import IrcNetwork.IrcEvent;


/**
 * AIBO bot realization
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
        this.ircNetwork = new IrcNetwork(
                Configuration.getConfigurationHashMap().get("IrcConnection.host"),
                Integer.parseInt(Configuration.getConfigurationHashMap().get("IrcConnection.port")),
                this);

        this.taskManager = new TaskManager(this.ircNetwork.getMessageSender());

        this.ircNetwork.connect();
    }

    public AIBO(String[] extensionNames) {
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
}
