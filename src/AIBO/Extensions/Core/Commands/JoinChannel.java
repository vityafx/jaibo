package AIBO.Extensions.Core.Commands;

import AIBO.Extensions.Command;
import AIBO.Extensions.Core.*;
import AIBO.Extensions.Core.Object;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public final class JoinChannel extends Command {
    private Object object;

    private String channel;

    public Command command;

    public JoinChannel() {

    }

    public JoinChannel(Object object) {
        this.object = object;
    }

    @Override
    public boolean check(String channel) {
        boolean checkPassed = false;

        Pattern p = Pattern.compile("#(.*)");   // Needs better regex

        CharSequence sequence = channel.subSequence(0, channel.length());
        Matcher matcher = p.matcher(sequence);

        if (matcher.matches()) {
            this.channel = channel;

            checkPassed = true;
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        this.object.getExtensionMessenger().getCommandSender().sendIrcCommand("JOIN", this.channel);
    }
}
