package aibo.systemextensions;


import aibo.ExtensionManager;
import aibo.ircnetwork.IrcMessageSender;
import org.jaibo.api.ExtensionMessengerInterface;
import org.jaibo.api.IrcCommandSenderInterface;

import java.util.HashMap;

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
    private final HashMap<String, String> topics = new HashMap<String, String>();
    private String messageOfTheDay;
    private String fullTopicMessage;


    public ExtensionMessenger(IrcMessageSender sender, ExtensionManager manager) {
        this.sender = sender;
        this.manager = manager;
    }


    private void updateTopic(String[] channels) {
        StringBuilder topicBuilder = new StringBuilder();

        for (String topic : this.topics.values()) {
            topicBuilder.append(String.format("%s | ", topic));
        }

        if (this.messageOfTheDay != null && !this.messageOfTheDay.isEmpty()) {
            topicBuilder.append(this.messageOfTheDay);
        }

        this.fullTopicMessage = topicBuilder.toString();

        this.setTopic(channels, this.fullTopicMessage);
    }

    @Override
    public void setTopicForExtension(String[] channels, String extensionName, String topic) {
        this.topics.put(extensionName, topic);

        this.updateTopic(channels);
    }

    @Override
    public void setMessageOfTheDay(String[] channels, String messageOfTheDay) {
        this.messageOfTheDay = messageOfTheDay;

        this.updateTopic(channels);
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
        this.sender.sendBroadcastMessage(channels, message);
    }

    @Override
    public void setTopic(String channel, String topicContent) {
        this.sender.setTopic(channel, topicContent);
    }

    @Override
    public void setTopic(String[] channels, String topicContent) {
        for (String channel : channels) {
            this.sender.setTopic(channel, topicContent);
        }
    }

    public IrcCommandSenderInterface getCommandSender() {
        return this.sender.getCommandSender();
    }
}
