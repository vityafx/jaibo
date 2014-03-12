package aibo.extensions.core.commands.messagelisteners;

import aibo.extensions.Command;
import aibo.extensions.Extension;
import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import ircnetwork.IrcUser;
import ircnetwork.MessageListener;

/**
 * Performs bot shutdown
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

public final class Shutdown extends Command implements MessageListener {
    private Extension object;
    private String receiver;

    public Shutdown() {
        this.addName("!shutdown");
    }

    public Shutdown(Extension object) {
        this();

        this.object = object;
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.PrivateMessage && this.check(message.getMessage())) {
            IrcUser user = IrcUser.tryParseFromIrcMessage(message.getFullMessage());

            if (user != null && this.object.isAdminHost(user.getHost())) {
                this.receiver = message.getUser();

                this.execute();
            }
        }
    }

    @Override
    protected void action() {
        this.object.getExtensionMessenger().sendPrivateMessage(this.receiver, "Goodbye! But will now shutdown.");

        System.exit(0);
    }
}