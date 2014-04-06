package aibo.systemextensions.core.commands.messagelisteners;

import org.jaibo.api.Command;
import aibo.systemextensions.core.Object;
import org.jaibo.api.IrcMessage;
import org.jaibo.api.IrcUser;
import org.jaibo.api.MessageListener;

/**
 * Gives operator privileges to a user with host = root_admin_host
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

public final class GetOp extends Command implements MessageListener {
    private Object object;
    private IrcUser ircUser;

    public GetOp() {
        this.addName("!get_op");
    }

    public GetOp(Object object) {
        this();

        this.object = object;
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (this.check(message.getMessage())) {
            this.ircUser = IrcUser.tryParseFromIrcMessage(message.getFullMessage());

            if (this.ircUser != null && this.object.isAdminHost(this.ircUser.getHost())) {
                this.execute();
            }
        }
    }

    @Override
    protected void action() {
        for(String channel : this.object.getChannels()) {
            this.object.getExtensionMessenger().getCommandSender().sendIrcCommand("MODE",
                    String.format("%s +o %s", channel, this.ircUser.getNick()));
        }
    }
}
