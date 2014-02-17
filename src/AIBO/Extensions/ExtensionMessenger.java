package AIBO.Extensions;


import AIBO.ExtensionManager;
import IrcNetwork.IrcCommandSender;
import IrcNetwork.IrcMessageSender;

/**
 * Extension message sender
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

public final class ExtensionMessenger implements ExtensionMessengerInterface {
    private IrcMessageSender sender;
    private ExtensionManager manager;


    public ExtensionMessenger(IrcMessageSender sender, ExtensionManager manager) {
        this.sender = sender;
        this.manager = manager;
    }

    @Override
    public void sendNotice(String username, String message) {
        this.sender.sendNotice(username, message);
    }

    @Override
    public void sendPrivateMessage(String username, String message) {
        this.sender.sendPrivateMessage(username, message);
    }

    @Override
    public void sendChannelMessage(String channel, String message) {
        this.sender.sendChannelMessage(channel, message);
    }

    @Override
    public void sendBroadcastMessage(String[] channels, String message) {
        for(String channel : channels) {
            this.sender.sendChannelMessage(channel, message);
        }
    }

    @Override
    public void setTopic(String channel, String topicContent) {
        this.sender.setTopic(channel, topicContent);
    }

    @Override
    public void sendExtensionMessage(String extensionName, ExtensionMessage message) {
        Extension extension = this.manager.getCurrentlyRunningExtensionByName(extensionName);

        if (extension != null) {
            extension.processTask(message);
        }
    }

    public IrcCommandSender getCommandSender() {
        return this.sender.getCommandSender();
    }
}
