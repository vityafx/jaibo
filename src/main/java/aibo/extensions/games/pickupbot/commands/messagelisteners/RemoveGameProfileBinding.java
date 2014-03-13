package aibo.extensions.games.pickupbot.commands.messagelisteners;

import aibo.extensions.Command;
import aibo.extensions.games.pickupbot.Object;
import helpers.ConfigurationListener;
import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import ircnetwork.IrcUser;
import ircnetwork.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Remove game profile binding
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

public final class RemoveGameProfileBinding extends Command implements MessageListener, ConfigurationListener {
    private aibo.extensions.games.pickupbot.Object object;

    private String receiver;
    private String host;
    private String gameProfile;

    public RemoveGameProfileBinding() {
        this.configurationChanged();
    }

    public RemoveGameProfileBinding(Object object) {
        this();

        this.object = object;
    }


    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = Object.Configuration.get("commands.remove_game_profile_binding").split(" ");

        this.addNames(names);
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.ChannelMessage && this.check(message.getMessage().trim())) {
            String receiverHost = IrcUser.tryParse(message.getUser() + "!" + message.getHost()).getHost();

            if (this.object.isAdminHost(receiverHost)) {
                this.receiver = message.getUser();

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
        if (Object.DatabaseManager.isGameProfileExistsForHost(this.host)
                && Object.DatabaseManager.getGameProfileForHost(host).equals(this.gameProfile)) {

            Object.DatabaseManager.removeHostBindingForGameProfile(this.host, this.gameProfile);

            this.object.getExtensionMessenger().sendNotice(this.receiver,
                    String.format("Binding with host=[%s] and game profile=[%s] has been removed successfully",
                            this.host, this.gameProfile));
        } else {
            this.object.getExtensionMessenger().sendNotice(this.receiver,
                    String.format("No associated game profile records exists for host=[%s] and game profile=[%s]",
                            this.host, this.gameProfile));
        }
    }
}