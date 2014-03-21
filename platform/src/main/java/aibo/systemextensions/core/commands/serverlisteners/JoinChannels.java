package aibo.systemextensions.core.commands.serverlisteners;

import org.jaibo.api.Extension;
import org.jaibo.api.SimpleCommand;

/**
 * Command to join a channel in irc network
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

public final class JoinChannels extends SimpleCommand {
    public JoinChannels(Extension object) {
        super(object);
    }

    @Override
    public void execute() {
        String[] channels = this.object.getChannels();

        for (String channel : channels) {
            this.object.getExtensionMessenger().getCommandSender().sendIrcCommand("JOIN",
                    String.format("%s", channel));
        }
    }
}
