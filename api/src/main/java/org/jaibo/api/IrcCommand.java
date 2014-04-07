package org.jaibo.api;

/**
 * Class describes irc command
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

public final class IrcCommand {
    private String name;
    private String arguments;

    public IrcCommand() {

    }

    public IrcCommand(String name, String arguments) {
        this.setName(name);
        this.setArguments(arguments);
    }

    public IrcCommand(IrcCommand ircCommand) {
        if (ircCommand != null) {
            this.setName(ircCommand.name);
            this.setArguments(ircCommand.arguments);
        }
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.getName(), this.getArguments());
    }
}
