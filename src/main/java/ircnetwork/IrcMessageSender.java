package ircnetwork;

import networkconnection.NetworkConnection;

/**
 * Message sender realization
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

public class IrcMessageSender implements IrcMessageSenderInterface {
    protected IrcCommandSender sender;


    protected IrcMessageSender() {

    }

    public IrcMessageSender(NetworkConnection connection) {
        this.sender = new IrcCommandSender(connection);
    }


    @Override
    public void sendNotice(String username, String message) {
        this.sender.sendIrcCommand("NOTICE", String.format("%s :%s", username, message));
    }

    @Override
    public void sendPrivateMessage(String username, String message) {
        this.sender.sendIrcCommand("PRIVMSG", String.format("%s :%s", username, message));
    }

    @Override
    public void sendChannelMessage(String channel, String message) {
        this.sender.sendIrcCommand("PRIVMSG", String.format("%s :%s", channel, message));
    }

    @Override
    public void sendBroadcastMessage(String[] channels, String message) {
        for(String channel : channels) {
            this.sender.sendIrcCommand("PRIVMSG", String.format("%s :%s", channel, message));
        }
    }

    @Override
    public void setTopic(String channel, String topicContent) {
        this.sender.sendIrcCommand("TOPIC", String.format("%s :%s", channel, topicContent));
    }

    @Override
    public void setTopic(String[] channels, String topicContent) {
        for (String channel : channels) {
            this.setTopic(channel, topicContent);
        }
    }

    public IrcCommandSender getCommandSender() {
        return this.sender;
    }
}
