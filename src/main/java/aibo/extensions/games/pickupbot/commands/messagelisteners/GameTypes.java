package aibo.extensions.games.pickupbot.commands.messagelisteners;

import aibo.extensions.Command;
import aibo.extensions.games.pickupbot.*;
import aibo.extensions.games.pickupbot.Object;
import helpers.ConfigurationListener;
import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import ircnetwork.IrcUser;
import ircnetwork.MessageListener;

/**
 * Shows what game type are available
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

public final class GameTypes extends Command implements MessageListener, ConfigurationListener {
    private aibo.extensions.games.pickupbot.Object object;
    private String receiver;

    public GameTypes() {
        this.configurationChanged();
    }

    public GameTypes(Object object) {
        this();

        this.object = object;
    }

    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = Object.Configuration.get("commands.game_types").split(" ");

        this.addNames(names);
    }
    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.ChannelMessage && this.checkExact(message.getMessage())) {
            this.receiver = message.getNick();

            this.execute();
        }
    }

    @Override
    protected void action() {
        String gameTypes = this.object.getGameTypesAsString(", ");

        this.object.getExtensionMessenger().sendNotice(this.receiver,
                String.format("Available game types are: %s", gameTypes));
    }
}