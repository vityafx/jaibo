package aibo.extensions.core.commands.serverlisteners;

import aibo.AIBO;
import aibo.extensions.Extension;
import aibo.extensions.SimpleCommand;
import helpers.ConfigurationListener;

/**
 * Authentication in irc network
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

public final class Auth extends SimpleCommand implements ConfigurationListener {
    private boolean authenticationNeed;
    private String username;
    private String password;

    private boolean authenticated;


    public Auth(Extension object) {
        super(object);

        AIBO.Configuration.addListener(this);

        this.readAuthenticationSettings();
    }

    private void readAuthenticationSettings() {
        String authenticationNeed = AIBO.Configuration.get("Authentication.authentication");

        if (authenticationNeed != null && authenticationNeed.equalsIgnoreCase("yes")) {
            this.authenticationNeed = true;

            this.username = AIBO.Configuration.get("Authentication.username");
            this.password = AIBO.Configuration.get("Authentication.password");
        }
    }

    @Override
    public void configurationChanged() {
        this.readAuthenticationSettings();
    }

    @Override
    public void execute() {
        if (!this.authenticated && this.authenticationNeed) {
            this.object.getExtensionMessenger().getCommandSender().sendIrcCommand("AUTH",
                    String.format("%s %s", this.username, this.password));

            this.authenticated = true;
        }
    }
}
