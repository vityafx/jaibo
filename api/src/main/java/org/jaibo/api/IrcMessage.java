package org.jaibo.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Irc Channel Message
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

public final class IrcMessage {
    private IrcMessageType messageType;
    private String user;
    private String host;
    private String receiver;
    private String message;
    private String fullMessage;


    public static IrcMessage tryParse(String message) {
        IrcMessage channelMessage = null;

        if(message != null) {
            Pattern p = Pattern.compile("^:(.*)!(.*) PRIVMSG ([#\\-\\w+]+) :(.*)$", Pattern.DOTALL);

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                channelMessage = new IrcMessage();
                channelMessage.setFullMessage(message);

                channelMessage.setUser(matcher.group(1));
                channelMessage.setHost(matcher.group(2));
                channelMessage.setReceiver(matcher.group(3));
                channelMessage.setMessage(matcher.group(4));
            }
        }

        return channelMessage;
    }


    public void setUser(String user) {
        this.user = user;
    }

    public String getNick() {
        return this.user;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;

        if (receiver.charAt(0) == '#') {
            this.setMessageType(IrcMessageType.ChannelMessage);
        } else {
            this.setMessageType(IrcMessageType.PrivateMessage);
        }
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public IrcMessageType getMessageType() {
        return this.messageType;
    }

    public void setMessageType(IrcMessageType messageType) {
        this.messageType = messageType;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public String toString() {
        return String.format("User: %s\nHost: %s\nChannel: %s\nMessage: %s\n",
                this.getNick(),
                this.getHost(),
                this.getReceiver(),
                this.getMessage());
    }
}
