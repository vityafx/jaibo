package other.advertisement.messagelisteners;

import org.jaibo.api.*;
import org.jaibo.api.helpers.ConfigurationListener;

import other.advertisement.ExtensionObject;

/**
 *
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

public final class ShowAds extends Command implements MessageListener, ConfigurationListener {
    private ExtensionObject object;

    private String receiver;
    private short advertisementId;


    public ShowAds() {
        this.configurationChanged();
    }

    public ShowAds(ExtensionObject object) {
        this();

        this.object = object;
    }


    public void configurationChanged() {
        this.clearNames();

        String[] names = ExtensionObject.Configuration.get("commands.show_ads").split(" ");

        this.addNames(names);
    }

    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.PrivateMessage && this.checkExact(message.getMessage().trim())) {
            IrcUser ircUser = IrcUser.tryParseFromIrcMessage(message.getFullMessage());

            if (ircUser != null && this.object.isAdminHost(ircUser.getHost())) {
                this.receiver = ircUser.getNick();

                this.execute();
            }
        }
    }
    @Override
    protected void action() {
        Short[] ids = this.object.getAdvertisementIds();
        String answer = "Currently live advertisements: [%s]";
        String idsString = "";

        for (short i = 0; i < ids.length; i++) {
            Short value = ids[i];

            idsString += String.valueOf(value);

            if (i != ids.length - 1) {
                idsString += ", ";
            }
        }

        this.object.getExtensionMessenger().sendPrivateMessage(this.receiver, String.format(answer, idsString));
    }
}