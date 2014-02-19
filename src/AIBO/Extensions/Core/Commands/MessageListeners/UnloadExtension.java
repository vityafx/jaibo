package AIBO.Extensions.Core.Commands.MessageListeners;

import AIBO.Extensions.Command;
import Errors.ExtensionError;
import Errors.ExtensionManagerError;
import IrcNetwork.IrcMessage;
import IrcNetwork.IrcMessageType;
import IrcNetwork.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unloads extension at runtime
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

public final class UnloadExtension extends Command implements MessageListener {
    private AIBO.Extensions.Core.Object object;
    private String receiver;
    private String extensionName;

    public UnloadExtension() {
        this.addName("!unload");
    }

    public UnloadExtension(AIBO.Extensions.Core.Object object) {
        this();

        this.object = object;
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.PrivateMessage && this.check(message.getMessage())) {
            this.receiver = message.getUser();

            this.execute();
        }
    }

    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        for (String name : this.getNames()) {
            Pattern p = Pattern.compile(String.format("^%s (.*)\r\n$", name));

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                this.extensionName = matcher.group(1);

                checkPassed = true;

                break;
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        try {
            this.object.getExtensionManager().removeExtensionByName(this.extensionName);

            if (this.object.getExtensionManager().getCurrentlyRunningExtensionByName(this.extensionName) == null) {
                this.object.getExtensionMessenger().sendPrivateMessage(this.receiver,
                        String.format("'%s' extension successfully unloaded", this.extensionName));
            }

            this.extensionName = null;
        } catch (ExtensionManagerError e) {
            this.object.getExtensionMessenger().sendPrivateMessage(this.receiver, e.getMessage());
        } catch (ExtensionError e) {
            this.object.getExtensionMessenger().sendPrivateMessage(this.receiver, e.getMessage());
        }
    }
}
