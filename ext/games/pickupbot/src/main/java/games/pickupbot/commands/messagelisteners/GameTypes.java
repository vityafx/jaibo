package games.pickupbot.commands.messagelisteners;

import games.pickupbot.ExtensionObject;
import org.jaibo.api.Command;
import org.jaibo.api.helpers.ConfigurationListener;
import org.jaibo.api.IrcMessage;
import org.jaibo.api.IrcMessageType;
import org.jaibo.api.MessageListener;

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
    private ExtensionObject object;
    private String receiver;

    public GameTypes() {
        this.configurationChanged();
    }

    public GameTypes(ExtensionObject object) {
        this();

        this.object = object;
    }

    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = ExtensionObject.Configuration.get("commands.game_types").split(" ");

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