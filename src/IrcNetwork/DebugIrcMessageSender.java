package IrcNetwork;

/**
 * Debug irc message sender
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

public final class DebugIrcMessageSender extends IrcMessageSender {
    public boolean setTopicEvent;
    public boolean sendChannelMessageEvent;
    public boolean sendBroadcastMessageEvent;
    public boolean sendPrivateMessageEvent;
    public boolean sendNoticeEvent;

    public DebugIrcMessageSender() {
        this.sender = new DebugIrcCommandSender();
    }

    @Override
    public void setTopic(String channel, String topicContent) {
        super.setTopic(channel, topicContent);

        this.setTopicEvent = true;
    }

    @Override
    public void setTopic(String[] channels, String topicContent) {
        super.setTopic(channels, topicContent);

        this.setTopicEvent = true;
    }

    @Override
    public void sendBroadcastMessage(String[] channels, String message) {
        super.sendBroadcastMessage(channels, message);

        this.sendBroadcastMessageEvent = true;
    }

    @Override
    public void sendChannelMessage(String channel, String message) {
        super.sendChannelMessage(channel, message);

        this.sendChannelMessageEvent = true;
    }

    @Override
    public void sendNotice(String username, String message) {
        super.sendNotice(username, message);

        this.sendNoticeEvent = true;
    }

    @Override
    public void sendPrivateMessage(String username, String message) {
        super.sendPrivateMessage(username, message);

        this.sendPrivateMessageEvent = true;
    }


    public boolean isSetTopicEvent() {
        boolean value = this.setTopicEvent;

        this.setTopicEvent = false;

        return value;
    }

    public boolean isSendChannelMessageEvent() {
        boolean value = this.sendChannelMessageEvent;

        this.sendChannelMessageEvent = false;

        return value;
    }

    public boolean isSendBroadcastMessageEvent() {
        boolean value = this.sendBroadcastMessageEvent;

        this.sendBroadcastMessageEvent = false;

        return value;
    }

    public boolean isSendPrivateMessageEvent() {
        boolean value = this.sendPrivateMessageEvent;

        this.sendPrivateMessageEvent = false;

        return value;
    }

    public boolean isSendNoticeEvent() {
        boolean value = this.sendNoticeEvent;

        this.sendNoticeEvent = false;

        return value;
    }
}