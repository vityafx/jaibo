package aibo.extensions.games.pickupbot.commands.messagelisteners;

import aibo.extensions.Command;
import aibo.extensions.games.pickupbot.*;
import aibo.extensions.games.pickupbot.errors.GameError;
import aibo.extensions.games.pickupbot.Object;
import helpers.ConfigurationListener;
import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import ircnetwork.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Outputs a time spent since last pickup game was played
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

public final class Lastgame extends Command implements MessageListener, ConfigurationListener {
    private Object object;

    private Player receiver;
    private String gameType;

    public Lastgame() {
        this.configurationChanged();
    }

    public Lastgame(Object object) {
        this();

        this.object = object;
    }


    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = Object.Configuration.get("commands.last_game").split(" ");

        this.addNames(names);
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.ChannelMessage && this.check(message.getMessage().trim())) {
            this.receiver = new Player(message.getUser(), message.getHost());

            this.execute();
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
                    this.gameType = matcher.group(1);

                    checkPassed = true;

                    break;
                }
            }

            if (!checkPassed) {
                for (String name : this.getNames()) {
                    if (message.equalsIgnoreCase(name)) {
                        checkPassed = true;

                        break;
                    }
                }
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        try {
            String lastGameString = this.object.lastGame(this.gameType);

            this.object.getExtensionMessenger().sendBroadcastMessage(this.object.getChannels(), lastGameString);
        } catch (GameError e) {
            this.object.getExtensionMessenger().sendNotice(this.receiver.getFormattedNickName(), e.getMessage());
        } finally {
            this.receiver = null;
            this.gameType = null;
        }
    }
}