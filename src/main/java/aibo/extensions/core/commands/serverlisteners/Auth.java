package aibo.extensions.core.commands.serverlisteners;

import aibo.AIBO;
import aibo.extensions.SimpleCommand;
import aibo.extensions.core.Object;
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
    private boolean setHiddenHostNeeded;

    private boolean authenticated;


    public Auth(Object object) {
        super(object);

        AIBO.Configuration.addListener(this);

        this.readAuthenticationSettings();
    }

    private void readAuthenticationSettings() {
        if (AIBO.Configuration.getBoolean("Authentication.authentication")) {
            this.authenticationNeed = true;

            this.username = AIBO.Configuration.get("Authentication.username");
            this.password = AIBO.Configuration.get("Authentication.password");

            this.setHiddenHostNeeded = AIBO.Configuration.getBoolean("ircconnection.hide_host");
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

            if (this.setHiddenHostNeeded) {
                Object coreObject = (Object)this.object;

                this.object.getExtensionMessenger().getCommandSender().sendIrcCommand("MODE",
                        String.format("%s +x", coreObject.getCurrentNickName()));
            }

            this.authenticated = true;
        }
    }
}
