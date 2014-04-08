package aibo.systemextensions.core.commands.messagelisteners;

import aibo.systemextensions.core.Object;
import org.jaibo.api.*;

/**
 * Command to show list of currently running extensions
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

public final class ExtensionsList extends Command implements MessageListener {
    private Object object;
    private IrcUser ircUser;

    public ExtensionsList() {
        this.addName("!extensions");
    }

    public ExtensionsList(Object object) {
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
        String[] extensionNames = this.object.getExtensionManager().getAllExtensionList();
        String answer = "";

        for (int i = 0; i < extensionNames.length; i++) {

            answer += IrcMessageTextModifier.makeBold(extensionNames[i]);

            if (this.object.getExtensionManager().isExtensionAlreadyAdded(extensionNames[i])) {
                answer += " (on)";
            } else {
                answer += " (off)";
            }

            if (i != extensionNames.length - 1) {
                answer += ", ";
            }
        }

        this.object.getExtensionMessenger().sendPrivateMessage(this.ircUser.getNick(), answer);
    }
}
