package aibo.extensions.core.commands.messagelisteners;

import aibo.ExtensionManager;
import aibo.extensions.Command;
import aibo.extensions.Extension;
import aibo.extensions.core.Object;
import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import ircnetwork.IrcUser;
import ircnetwork.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command adds admin to the database
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


public final class AddAdmin extends Command implements MessageListener {
    private Object object;
    private String extensionName;
    private String adminHost;
    private IrcUser ircUser;

    public AddAdmin() {
        this.addName("!add_admin");
    }

    public AddAdmin(Object object) {
        this();

        this.object = object;
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.PrivateMessage && this.check(message.getMessage().trim())) {

            this.ircUser = IrcUser.tryParseFromIrcMessage(message.getFullMessage());

            if (this.ircUser != null && this.object.isAdminHost(this.ircUser.getHost())) {
                this.execute();
            }
        }
    }

    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        if (super.check(message)) {
            for (String name : this.getNames()) {
                Pattern p = Pattern.compile(String.format("^%s (.*) (.*)$", name), Pattern.CASE_INSENSITIVE);

                CharSequence sequence = message.subSequence(0, message.length());
                Matcher matcher = p.matcher(sequence);

                if (matcher.matches()) {
                    this.extensionName = matcher.group(1);
                    this.adminHost = matcher.group(2);

                    checkPassed = true;

                    break;
                }
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        if (ExtensionManager.IsExtensionObjectExists(this.extensionName)) {
            if (!Extension.MainDatabaseManager.isAdminExists(this.extensionName, this.adminHost)) {

                Extension.MainDatabaseManager.addAdmin(this.extensionName, this.adminHost);

                if (Extension.MainDatabaseManager.isAdminExists(this.extensionName, this.adminHost)) {
                    this.object.getExtensionMessenger().sendPrivateMessage(this.ircUser.getNick(),
                            String.format("Admin with host=[%s] has been added to extension \"%s\"",
                                    this.adminHost, this.extensionName));
                } else {
                    this.object.getExtensionMessenger().sendPrivateMessage(this.ircUser.getNick(),
                            String.format("Admin with host=[%s] was not added to extension \"%s\"",
                                    this.adminHost, this.extensionName));
                }
            } else {
                this.object.getExtensionMessenger().sendPrivateMessage(this.ircUser.getNick(),
                        String.format("Admin with host=[%s] has been added already to extension \"%s\"",
                                this.adminHost, this.extensionName));
            }
        } else {
            this.object.getExtensionMessenger().sendPrivateMessage(this.ircUser.getNick(),
                    String.format("Extension \"%s\" is not exists", this.extensionName));
        }
    }
}
