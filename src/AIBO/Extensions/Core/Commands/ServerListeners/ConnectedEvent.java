package AIBO.Extensions.Core.Commands.ServerListeners;

import AIBO.Extensions.Command;
import AIBO.Extensions.SimpleCommand;
import IrcNetwork.ServerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Launches when after bot finished to connect to irc server
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

public final class ConnectedEvent extends Command implements ServerListener {
    public ArrayList<SimpleCommand> commands = new ArrayList<SimpleCommand>();


    public ConnectedEvent(SimpleCommand[] commands) {
        this.commands = new ArrayList<SimpleCommand>(Arrays.asList(commands));
    }

    public ConnectedEvent(SimpleCommand command) {
        if (command != null) {
            this.commands.add(command);
        }
    }

    @Override
    public void serverMessageReceived(String message) {
        this.checkAndExecute(message);
    }


    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        Pattern p = Pattern.compile("^:(.*) 001 jaibo :(.*)$", Pattern.DOTALL | Pattern.MULTILINE);

        CharSequence sequence = message.subSequence(0, message.length());
        Matcher matcher = p.matcher(sequence);

        if (matcher.matches()) {
            checkPassed = true;
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        if (this.commands != null) {
            for(SimpleCommand command : this.commands) {
                command.execute();
            }
        }
    }
}
