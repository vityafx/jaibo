package aibo.extensions.other.advertisement.messagelisteners;

import aibo.extensions.Command;
import aibo.extensions.other.advertisement.Object;
import aibo.extensions.other.advertisement.errors.AdvertisementError;
import helpers.ConfigurationListener;
import ircnetwork.IrcMessage;
import ircnetwork.IrcMessageType;
import ircnetwork.MessageListener;

/**
 * Removes advertisement
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

public final class RemoveAd extends Command implements MessageListener, ConfigurationListener {
    private aibo.extensions.other.advertisement.Object object;

    private String receiver;


    public RemoveAd() {
        this.configurationChanged();
    }

    public RemoveAd(Object object) {
        this();

        this.object = object;
    }


    @Override
    public void configurationChanged() {
        this.clearNames();

        String[] names = Object.Configuration.get("commands.remove_ad").split(" ");

        this.addNames(names);
    }

    @Override
    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.PrivateMessage && this.check(message.getMessage().trim())) {
            this.receiver = message.getUser();

            this.execute();
        }
    }

    @Override
    protected void action() {
        try {
            this.object.removeAdvertisement();

            this.object.getExtensionMessenger().sendPrivateMessage(this.receiver,
                    "Advertisement has been removed successfully.");
        } catch (AdvertisementError e) {
            this.object.getExtensionMessenger().sendNotice(this.receiver, e.getMessage());
        }
    }
}