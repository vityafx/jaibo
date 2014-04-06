package other.greeting;

import org.jaibo.api.Extension;
import other.greeting.eventlisteners.Join;
import other.greeting.messagelisteners.RemoveGreetingMessage;
import other.greeting.messagelisteners.SetGreetingMessage;
import org.jaibo.api.helpers.Configuration;
import org.jaibo.api.helpers.ConfigurationListener;
import org.jaibo.api.IrcUser;

/**
 * Greets people when they are joins the channel
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

public final class ExtensionObject extends Extension implements ConfigurationListener {
    public final static Configuration Configuration = new Configuration("Other.Greeting.ini");


    @Override
    public String getExtensionName() {
        return "other.greeting";
    }

    @Override
    protected void setCommands() {
        this.addEventListener(new Join(this));

        this.addMessageListener(new SetGreetingMessage(this));
        this.addMessageListener(new RemoveGreetingMessage(this));
    }

    @Override
    public String getHelpPage() {
        return null;
    }

    public void setGreeting(IrcUser user, String greetingMessage) {

    }

    public void removeGreeting(IrcUser user) {

    }

    public void showGreetingMessage(IrcUser user) {
        // if user exists in the database - show appropriate message
        // else do nothing
    }

    @Override
    public void configurationChanged() {
        super.configurationChanged();

    }
}
