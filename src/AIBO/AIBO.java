package AIBO;

import java.util.ArrayList;

import AIBO.Extensions.TaskManager;
import IrcNetwork.IrcNetwork;
import IrcNetwork.MessageListener;
import IrcNetwork.ServerListener;
import IrcNetwork.EventListener;
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
    private ExtensionManager extensionManager = new ExtensionManager(new String[]{ "core" });
    private TaskManager taskManager = new TaskManager(this.extensionManager);

    private IrcNetwork ircNetwork = new IrcNetwork("irc.quakenet.org", 6667, this);


    public AIBO(String[] extensionNames) {
        this.extensionManager.addExtensionsByNames(extensionNames);

        this.setIrcListeners();
    }

    @Override
    public void ircMessageReceived(String message) {
        if (message != null) {
            IrcMessage ircMessage = IrcMessage.tryParse(message);
            IrcEvent ircEvent = IrcEvent.tryParse(message);

            if (ircMessage != null) {
                this.notifyMessageListeners(ircMessage);
            } else if (ircEvent != null) {
                this.notifyEventListeners(ircEvent);
            } else {
                this.notifyServerListeners(message);
            }
        }
    }

    private void notifyMessageListeners(IrcMessage ircMessage) {
        for(MessageListener listener : messageListeners) {
            listener.messageReceived(ircMessage);
        }
    }

    private void notifyEventListeners(IrcEvent ircEvent) {
        for(EventListener listener : eventListeners) {
            listener.eventReceived(ircEvent);
        }
    }

    private void notifyServerListeners(String serverMessage) {
        for(ServerListener listener : serverListeners) {
            listener.serverMessageReceived(serverMessage);
        }
    }
}
