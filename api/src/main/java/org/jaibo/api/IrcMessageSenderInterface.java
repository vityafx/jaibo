package org.jaibo.api;

/**
 * Message sender interface
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

public interface IrcMessageSenderInterface {
    public void sendNotice(String username, String message);

    public void sendPrivateMessage(String username, String message);

    public void sendChannelMessage(String channel, String message);

    public void sendBroadcastMessage(String[] channels, String message);

    public void setTopic(String channel, String topicContent);

    public void setTopic(String[] channels, String topicContent);
}
