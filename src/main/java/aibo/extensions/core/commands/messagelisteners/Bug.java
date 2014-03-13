package aibo.extensions.core.commands.messagelisteners;

import aibo.AIBO;
import aibo.extensions.Command;
import aibo.extensions.core.Object;
import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import ircnetwork.MessageListener;

/**
 * Gives links to send a bug report
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

public final class Bug extends Command implements MessageListener {
    private Object object;

    public Bug() {
        this.addName("!bug");
    }

    public Bug(Object object) {
        this();

        this.object = object;
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.ChannelMessage && this.checkExact(message.getMessage().trim())) {
            this.execute();
        }
    }

    @Override
    protected void action() {
        this.object.getExtensionMessenger().sendBroadcastMessage(this.object.getChannels(),
                AIBO.Configuration.get("aibo.bugs"));
    }
}