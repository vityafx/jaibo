package aibo.extensions.core.commands.serverlisteners;

import aibo.extensions.Command;
import aibo.extensions.core.Object;
import ircnetwork.ServerListener;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pong answer
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

public final class Pong extends Command implements ServerListener {

    private String pongAnswer;
    private Object object;

    public Pong() {

    }

    public Pong(Object object) {
        this.object = object;
    }

    @Override
    public void serverMessageReceived(String message) {
        this.checkAndExecute(message);
    }

    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        Pattern p = Pattern.compile("^PING :(.*)$");

        CharSequence sequence = message.subSequence(0, message.length());
        Matcher matcher = p.matcher(sequence);

        if (matcher.matches()) {
            this.pongAnswer = String.format(matcher.group(1));

            checkPassed = true;
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        this.object.getExtensionMessenger().getCommandSender().sendIrcCommand("PONG", this.pongAnswer);
    }
}
