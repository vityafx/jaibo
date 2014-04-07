package org.jaibo.api.tests;

import org.jaibo.api.IrcCommand;
import org.jaibo.api.IrcCommandSenderInterface;

/**
 * Debug version of IrcCommandSender
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

public final class TestIrcCommandSender implements IrcCommandSenderInterface {

    @Override
    public void sendIrcCommand(IrcCommand command) {
        if (command != null) {
            System.out.println(String.format("Sending %s: %s", command.getName(), command.getArguments()));
        }
    }
}
