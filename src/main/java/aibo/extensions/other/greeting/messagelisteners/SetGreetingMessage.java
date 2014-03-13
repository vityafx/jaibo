package aibo.extensions.other.greeting.messagelisteners;

import aibo.extensions.Command;
import aibo.extensions.other.greeting.Object;
import helpers.ConfigurationListener;
import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import ircnetwork.IrcUser;
import ircnetwork.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sets greeting message
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

public final class SetGreetingMessage extends Command implements MessageListener, ConfigurationListener {
    private aibo.extensions.other.greeting.Object object;

    private IrcUser ircUser;
    private String greetingMessage;


    public SetGreetingMessage() {
        this.configurationChanged();
    }

    public SetGreetingMessage(Object object) {
        this();

        this.object = object;
    }


    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = aibo.extensions.games.pickupbot.Object.Configuration.get("commands.set_greeting").split(" ");

        this.addNames(names);
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.PrivateMessage && this.check(message.getMessage().trim())) {
            this.ircUser = IrcUser.tryParseFromIrcMessage(message.getFullMessage());

            if (this.ircUser != null && this.object.isAdminHost(this.ircUser.getHost())) {
                this.execute();
            }
        }
    }

    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        if (super.check(message)) {
            for (String name : this.getNames()) {
                Pattern p = Pattern.compile(String.format("^%s (.*)@(.*)$", name), Pattern.CASE_INSENSITIVE);

                CharSequence sequence = message.subSequence(0, message.length());
                Matcher matcher = p.matcher(sequence);

                if (matcher.matches()) {
                    this.ircUser = new IrcUser(null, matcher.group(1));
                    this.greetingMessage = matcher.group(2);

                    break;
                }
            }

            checkPassed = true;
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        this.object.setGreeting(this.ircUser, this.greetingMessage);
    }
}