package games.pickupbot.commands.messagelisteners;

import games.pickupbot.ExtensionObject;
import games.pickupbot.errors.PlayerError;

import org.jaibo.api.Command;
import org.jaibo.api.helpers.ConfigurationListener;
import org.jaibo.api.IrcMessage;
import org.jaibo.api.IrcMessageType;
import org.jaibo.api.IrcUser;
import org.jaibo.api.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sets binding between irc user host and game profile
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

public final class SetGameProfileBinding extends Command implements MessageListener, ConfigurationListener {
    private ExtensionObject object;

    private String receiver;
    private String host;
    private String gameProfile;

    public SetGameProfileBinding() {
        this.configurationChanged();
    }

    public SetGameProfileBinding(ExtensionObject object) {
        this();

        this.object = object;
    }


    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = ExtensionObject.Configuration.get("commands.set_game_profile_binding").split(" ");

        this.addNames(names);
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.ChannelMessage && this.check(message.getMessage().trim())) {
            String receiverHost = IrcUser.tryParse(message.getNick() + "!" + message.getHost()).getHost();

            if (this.object.isAdminHost(receiverHost)) {
                this.receiver = message.getNick();

                this.execute();
            }
        }
    }

    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        if (super.check(message)) {
            for (String name : this.getNames()) {
                Pattern p = Pattern.compile(String.format("^%s (.*) (.*)$", name), Pattern.CASE_INSENSITIVE);

                CharSequence sequence = message.subSequence(0, message.length());
                Matcher matcher = p.matcher(sequence);

                if (matcher.matches()) {
                    this.host = matcher.group(1);
                    this.gameProfile = matcher.group(2);

                    checkPassed = true;

                    break;
                }
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        try {
            this.object.addGameProfile(this.host, this.gameProfile);

            this.object.getExtensionMessenger().sendNotice(this.receiver,
                    String.format("games.pickupbot.Game profile=[%s] has been set successfully", this.gameProfile));
        } catch (PlayerError e) {
            this.object.getExtensionMessenger().sendNotice(this.receiver, e.getMessage());
        }
    }
}