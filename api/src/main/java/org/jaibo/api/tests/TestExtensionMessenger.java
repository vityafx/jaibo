package org.jaibo.api.tests;

import org.jaibo.api.ExtensionMessengerInterface;
import org.jaibo.api.IrcCommandSenderInterface;

/**
 * Extension messenger realization for test purposes
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

public class TestExtensionMessenger implements ExtensionMessengerInterface {
    public final TestIrcMessageSender messageSender = new TestIrcMessageSender();


    @Override
    public void setTopicForExtension(String[] channels, String extensionName, String topic) {
        this.setTopic(channels, "");
    }

    @Override
    public void setMessageOfTheDay(String[] channels, String messageOfTheDay) {
        this.setTopic(channels, "");
    }

    @Override
    public IrcCommandSenderInterface getCommandSender() {
        return new TestIrcCommandSender();
    }

    @Override
    public void sendNotice(String username, String message) {
        messageSender.sendNotice(username, message);
    }

    @Override
    public void sendPrivateMessage(String username, String message) {
        messageSender.sendPrivateMessage(username, message);
    }

    @Override
    public void sendChannelMessage(String channel, String message) {
        messageSender.sendChannelMessage(channel, message);
    }

    @Override
    public void sendBroadcastMessage(String[] channels, String message) {
        messageSender.sendBroadcastMessage(channels, message);
    }

    @Override
    public void setTopic(String channel, String topicContent) {
        messageSender.setTopic(channel, topicContent);
    }

    @Override
    public void setTopic(String[] channels, String topicContent) {
        messageSender.setTopic(channels, topicContent);
    }



    public boolean isSetTopicEvent() {
        return messageSender.isSetTopicEvent();
    }

    public boolean isSendChannelMessageEvent() {
        return messageSender.isSendChannelMessageEvent();
    }

    public boolean isSendBroadcastMessageEvent() {
        return messageSender.isSendBroadcastMessageEvent();
    }

    public boolean isSendPrivateMessageEvent() {
        return messageSender.isSendPrivateMessageEvent();
    }

    public boolean isSendNoticeEvent() {
        return messageSender.isSendNoticeEvent();
    }
}
