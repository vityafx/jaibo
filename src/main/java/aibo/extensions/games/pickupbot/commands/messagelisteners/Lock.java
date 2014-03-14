package aibo.extensions.games.pickupbot.commands.messagelisteners;

import aibo.extensions.Command;
import aibo.extensions.games.pickupbot.*;
import aibo.extensions.games.pickupbot.Object;
import aibo.extensions.games.pickupbot.errors.GameError;
import aibo.extensions.games.pickupbot.errors.PickupBotError;
import helpers.ConfigurationListener;
import helpers.GregorianCalendarCreator;
import helpers.GregorianCalendarDifference;
import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import ircnetwork.IrcUser;
import ircnetwork.MessageListener;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Locks player to add in pickup game
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

public final class Lock extends Command implements MessageListener, ConfigurationListener {
    private aibo.extensions.games.pickupbot.Object object;

    private String receiver;
    private String gameProfile;
    private GregorianCalendar unlockDate;

    public Lock() {
        this.configurationChanged();
    }

    public Lock(aibo.extensions.games.pickupbot.Object object) {
        this();

        this.object = object;
    }


    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = Object.Configuration.get("commands.lock").split(" ");

        this.addNames(names);
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.ChannelMessage) {
            this.receiver = message.getNick();

            if (!Object.Configuration.getBoolean("player.game_profile_required")) {
                this.object.getExtensionMessenger().sendNotice(this.receiver,
                        "You have to enable game profile feature first");
            } else if (this.check(message.getMessage().trim())) {
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
            Pattern p = Pattern.compile(String.format("^%s (.*) (.*)$", name), Pattern.CASE_INSENSITIVE);

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                this.gameProfile = matcher.group(1);
                this.unlockDate = GregorianCalendarCreator.createFromCurrentDateByAppendingTimeString(matcher.group(2));

                if (this.unlockDate != null) {
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
            if (Object.DatabaseManager.isPlayerLocked(this.gameProfile)) {
                this.object.removePlayerFromEachGameType(new Player(null, null, this.gameProfile), true);

                String unlockDateTimeString = GregorianCalendarDifference.GetDifferenceAsHumanReadableString(this.unlockDate,
                        new GregorianCalendar());

                this.object.getExtensionMessenger().sendNotice(this.receiver,
                        String.format("Player with game profile=[%s] is already locked for next [%s]", this.gameProfile,
                                unlockDateTimeString));
            } else {
                this.object.removePlayerFromEachGameType(new Player(null, null, this.gameProfile), true);

                long unlockDateInMillis = this.unlockDate.getTimeInMillis();

                Object.DatabaseManager.addLockedPlayer(this.gameProfile, unlockDateInMillis);
            }
        } catch (PickupBotError e) {
            this.object.getExtensionMessenger().sendNotice(this.receiver, e.getMessage());
        } catch (GameError e) {
            this.object.getExtensionMessenger().sendNotice(this.receiver, e.getMessage());
        }
    }
}