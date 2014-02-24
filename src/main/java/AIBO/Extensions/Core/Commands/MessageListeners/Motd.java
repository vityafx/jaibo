package AIBO.Extensions.Core.Commands.MessageListeners;

import AIBO.Extensions.Command;
import AIBO.Extensions.Core.Object;
import IrcNetwork.IrcMessage;
import IrcNetwork.IrcMessageType;
import IrcNetwork.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sets message of the day on the channel
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

public final class Motd extends Command implements MessageListener {
    private Object object;
    private String messageOfTheDay;

    public Motd() {
        this.addName("!motd");
    }

    public Motd(Object object) {
        this();

        this.object = object;
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.ChannelMessage) {
            this.checkAndExecute(message.getMessage().trim());
        }
    }

    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        if (super.check(message)) {
            for (String name : this.getNames()) {
                Pattern p = Pattern.compile(String.format("^%s (.*)$", name), Pattern.CASE_INSENSITIVE);

                CharSequence sequence = message.subSequence(0, message.length());
                Matcher matcher = p.matcher(sequence);

                if (matcher.matches()) {
                    this.messageOfTheDay = matcher.group(1);

                    break;
                }
            }

            checkPassed = true;
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        this.object.getExtensionMessenger().setMessageOfTheDay(this.object.getChannels(), this.messageOfTheDay);
    }
}
