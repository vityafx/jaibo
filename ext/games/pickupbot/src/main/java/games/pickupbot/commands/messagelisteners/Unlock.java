package games.pickupbot.commands.messagelisteners;

import games.pickupbot.ExtensionObject;
import games.pickupbot.errors.GameError;
import games.pickupbot.errors.PickupBotError;

import org.jaibo.api.Command;
import org.jaibo.api.helpers.ConfigurationListener;
import org.jaibo.api.IrcMessage;
import org.jaibo.api.IrcMessageType;
import org.jaibo.api.IrcUser;
import org.jaibo.api.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unlocks locked player
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

public final class Unlock extends Command implements MessageListener, ConfigurationListener {
    private ExtensionObject object;

    private String receiver;
    private String gameProfile;

    public Unlock() {
        this.configurationChanged();
    }

    public Unlock(ExtensionObject object) {
        this();

        this.object = object;
    }


    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = ExtensionObject.Configuration.get("commands.unlock").split(" ");

        this.addNames(names);
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.ChannelMessage) {
            this.receiver = message.getNick();

            if (this.check(message.getMessage().trim())) {
                String receiverHost = IrcUser.tryParse(message.getNick() + "!" + message.getHost()).getHost();

                if (this.object.isAdminHost(receiverHost)) {
                    this.execute();
                }
            }
        }
    }

    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        for (String name : this.getNames()) {
            Pattern p = Pattern.compile(String.format("^%s (.*)$", name), Pattern.CASE_INSENSITIVE);

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                this.gameProfile = matcher.group(1);

                checkPassed = true;

                break;
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        try {
            if (ExtensionObject.DatabaseManager.isPlayerLocked(this.gameProfile)) {
                ExtensionObject.DatabaseManager.removeLockedPlayer(this.gameProfile);

                this.object.getExtensionMessenger().sendNotice(this.receiver,
                        String.format("games.pickupbot.Player with game profile=[%s] has been unlocked", this.gameProfile));
            } else {
                this.object.getExtensionMessenger().sendNotice(this.receiver,
                        String.format("games.pickupbot.Player with game profile=[%s] is not locked", this.gameProfile));
            }
        } catch (PickupBotError e) {
            this.object.getExtensionMessenger().sendNotice(this.receiver, e.getMessage());
        } catch (GameError e) {
            this.object.getExtensionMessenger().sendNotice(this.receiver, e.getMessage());
        }
    }
}