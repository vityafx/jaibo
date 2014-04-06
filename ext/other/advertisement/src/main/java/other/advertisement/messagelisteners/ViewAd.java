package other.advertisement.messagelisteners;

import other.advertisement.Advertisement;
import other.advertisement.ExtensionObject;

import org.jaibo.api.*;
import org.jaibo.api.helpers.ConfigurationListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * View advertisement by id
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

public final class ViewAd extends Command implements MessageListener, ConfigurationListener {
    private ExtensionObject object;

    private String receiver;
    private short advertisementId;


    public ViewAd() {
        this.configurationChanged();
    }

    public ViewAd(ExtensionObject object) {
        this();

        this.object = object;
    }


    public void configurationChanged() {
        this.clearNames();

        String[] names = ExtensionObject.Configuration.get("commands.view_ad").split(" ");

        this.addNames(names);
    }

    public void messageReceived(IrcMessage message) {
        if (message.getMessageType() == IrcMessageType.PrivateMessage && this.check(message.getMessage().trim())) {
            IrcUser ircUser = IrcUser.tryParseFromIrcMessage(message.getFullMessage());

            if (ircUser != null && this.object.isAdminHost(ircUser.getHost())) {
                this.receiver = ircUser.getNick();

                this.execute();
            }
        }
    }

    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        for (String name : this.getNames()) {
            Pattern p = Pattern.compile(String.format("^%s (\\d+)$", name), Pattern.CASE_INSENSITIVE);

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                this.advertisementId = Short.parseShort(matcher.group(1));

                checkPassed = true;

                break;
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        Advertisement advertisement = this.object.getAdvertisementById(this.advertisementId);
        String answer = String.format("No advertisement with id=[%d]", this.advertisementId);

        if (advertisement != null) {
            answer = advertisement.toString();
        }

        this.object.getExtensionMessenger().sendPrivateMessage(this.receiver, answer);
    }
}