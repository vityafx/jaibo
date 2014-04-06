package aibo.systemextensions.core.commands.messagelisteners;

import aibo.AIBO;
import aibo.systemextensions.core.Object;

import org.jaibo.api.Command;
import org.jaibo.api.IrcMessage;
import org.jaibo.api.IrcUser;
import org.jaibo.api.MessageListener;
import org.jaibo.api.helpers.GregorianCalendarHelper;

import java.util.GregorianCalendar;

/**
 * Uptime command shows system usage and uptime :)
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

public final class Uptime extends Command implements MessageListener {
    private Object object;
    private IrcUser ircUser;

    public Uptime() {
        this.addName("!uptime");
    }

    public Uptime(Object object) {
        this();

        this.object = object;
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (this.checkExact(message.getMessage())) {
            this.ircUser = IrcUser.tryParseFromIrcMessage(message.getFullMessage());

            if (this.ircUser != null && this.object.isAdminHost(this.ircUser.getHost())) {
                this.execute();
            }
        }
    }

    @Override
    protected void action() {
        String upTime = GregorianCalendarHelper.GetDifferenceAsHumanReadableString(
                new GregorianCalendar(),
                AIBO.getStartDateTime());

        System.gc();
        long usedMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;

        this.object.getExtensionMessenger().sendPrivateMessage(this.ircUser.getNick(),
                String.format("Uptime: %s | Memory usage: %d MB", upTime, usedMB));
    }
}