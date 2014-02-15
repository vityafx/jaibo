package AIBO.Extensions.Core;

import AIBO.Extensions.Core.Commands.MessageListeners.Hello;
import AIBO.Extensions.Core.Commands.ServerListeners.Auth;
import AIBO.Extensions.Core.Commands.ServerListeners.ConnectedEvent;
import AIBO.Extensions.Core.Commands.ServerListeners.Login;
import AIBO.Extensions.Core.Commands.ServerListeners.Pong;
import AIBO.Extensions.Extension;
import AIBO.Extensions.ExtensionMessenger;
import AIBO.Extensions.SimpleCommand;

/**
 * Core extension object
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

public class Object extends Extension {

    public Object() {
        this.addServerListener(new Pong(this));
        this.addServerListener(new Login(this));
        this.addMessageListener(new Hello(this));

        this.addServerListener(new ConnectedEvent(new SimpleCommand(this) {
                    @Override
                    public void execute() {
                        this.object.getExtensionMessenger().getCommandSender().sendIrcCommand("JOIN", "#ircbottest");
                    }
                },
                new Auth(this)));
    }

    public Object(ExtensionMessenger messenger) {
        this.setExtensionMessenger(messenger);
    }

    @Override
    public void run() {

    }

    @Override
    public String getExtensionName() {
        return "Core";
    }
}
