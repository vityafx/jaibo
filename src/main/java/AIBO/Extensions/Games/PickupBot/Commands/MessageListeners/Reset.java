package AIBO.Extensions.Games.PickupBot.Commands.MessageListeners;

import AIBO.Extensions.Command;
import AIBO.Extensions.Games.PickupBot.Object;
import Helpers.ConfigurationListener;
import IrcNetwork.IrcMessage;
import IrcNetwork.IrcMessageType;
import IrcNetwork.MessageListener;

/**
 * Resets pickup bot games
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

public final class Reset extends Command implements MessageListener, ConfigurationListener {
    private Object object;
    private String receiver;

    public Reset() {
        this.configurationChanged();
    }

    public Reset(Object object) {
        this();

        this.object = object;
    }

    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = Object.Configuration.getConfigurationHashMap().get("Commands.reset").split(" ");

        this.addNames(names);
    }
    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.PrivateMessage && this.check(message.getMessage())) {
            this.receiver = message.getUser();

            this.execute();
        }
    }

    @Override
    protected void action() {
        this.object.reset();

        this.object.getExtensionMessenger().sendPrivateMessage(this.receiver,
                String.format("%s extension has been reset successfully", this.object.getExtensionName()));
    }
}