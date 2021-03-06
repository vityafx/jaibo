package games.livestreams.messagelisteners;

import games.livestreams.ExtensionObject;

import games.livestreams.errors.ProviderError;
import org.jaibo.api.Command;
import org.jaibo.api.helpers.ConfigurationListener;
import org.jaibo.api.IrcMessage;
import org.jaibo.api.IrcMessageType;
import org.jaibo.api.MessageListener;

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
    private ExtensionObject object;
    private String providerName;
    private String streamTag;

    public Streams() {
        this.configurationChanged();
    }

    public Streams(ExtensionObject object) {
        this();

        this.object = object;
    }

    public void configurationChanged() {
        this.clearNames();

        String[] names = ExtensionObject.Configuration.get("commands.streams").split(" ");

        this.addNames(names);
    }

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
                this.streamTag = matcher.group(2);

                checkPassed = true;

                break;
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        try {
            String[] streams = this.object.getStreams(this.providerName, this.streamTag);

            for (String stream : streams) {
                this.object.getExtensionMessenger().sendBroadcastMessage(this.object.getChannels(), stream);
            }
        } catch (ProviderError e) {
            this.object.getExtensionMessenger().sendBroadcastMessage(this.object.getChannels(), e.getMessage());
        }
    }
}
