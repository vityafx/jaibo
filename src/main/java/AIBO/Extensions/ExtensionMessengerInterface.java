package AIBO.Extensions;

import IrcNetwork.IrcMessageSenderInterface;

/**
 * Extension messenger interface
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

public interface ExtensionMessengerInterface extends IrcMessageSenderInterface {
    public void sendExtensionMessage(String extensionName, ExtensionMessage message);

    public void setTopicForExtension(String[] channels, String extensionName, String topic);

    public void setMessageOfTheDay(String[] channels, String messageOfTheDay);
}