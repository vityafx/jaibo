package AIBO.Extensions.Games.LiveStreams.MessageListeners;

import AIBO.Extensions.Command;
import AIBO.Extensions.Games.LiveStreams.Object;
import Helpers.ConfigurationListener;
import IrcNetwork.IrcMessage;
import IrcNetwork.IrcMessageType;
import IrcNetwork.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shows currently live streams
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

public final class Streams extends Command implements MessageListener, ConfigurationListener {
    private Object object;
    private String providerName;
    private String streamTag;

    public Streams() {
        this.configurationChanged();
    }

    public Streams(Object object) {
        this();

        this.object = object;
    }

    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = Object.Configuration.getConfigurationHashMap().get("Commands.streams").split(" ");

        this.addNames(names);
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

        for (String name : this.getNames()) {
            Pattern p = Pattern.compile(String.format("^%s (.*)@(.*)$", name), Pattern.CASE_INSENSITIVE);

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                this.providerName = matcher.group(1);
                this.streamTag = matcher.group(1);

                checkPassed = true;

                break;
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        String[] streams = this.object.getStreams(this.providerName, this.streamTag);

        this.object.getExtensionMessenger().sendBroadcastMessage(this.object.getChannels(), streams.toString());
    }
}
