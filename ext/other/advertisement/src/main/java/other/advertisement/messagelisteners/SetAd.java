package other.advertisement.messagelisteners;

import other.advertisement.ExtensionObject;
import other.advertisement.errors.AdvertisementError;

import org.jaibo.api.Command;
import org.jaibo.api.helpers.ConfigurationListener;
import org.jaibo.api.IrcMessage;
import org.jaibo.api.IrcMessageType;
import org.jaibo.api.IrcUser;
import org.jaibo.api.MessageListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sets advertisement
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

public final class SetAd extends Command implements MessageListener, ConfigurationListener {
    private ExtensionObject object;

    private String receiver;
    private short timePeriod;
    private String advertisementText;


    public SetAd() {
        this.configurationChanged();
    }

    public SetAd(ExtensionObject object) {
        this();

        this.object = object;
    }


    public void configurationChanged() {
        this.clearNames();

        String[] names = ExtensionObject.Configuration.get("commands.set_ad").split(" ");

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
            Pattern p = Pattern.compile(String.format("^%s (\\d+) (.*)$", name), Pattern.CASE_INSENSITIVE);

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                this.timePeriod = Short.parseShort(matcher.group(1));
                this.advertisementText = matcher.group(2);

                checkPassed = true;

                break;
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        try {
            int id = this.object.setAdvertisement(this.advertisementText, this.timePeriod);

            this.object.getExtensionMessenger().sendPrivateMessage(this.receiver,
                    String.format("Advertisement has been set successfully, id=[%d]", id));
        } catch (AdvertisementError e) {
            this.object.getExtensionMessenger().sendNotice(this.receiver, e.getMessage());
        }
    }
}