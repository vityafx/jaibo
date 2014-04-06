package aibo.systemextensions.core.commands.serverlisteners;

import aibo.AIBO;
import org.jaibo.api.Command;
import aibo.systemextensions.core.Object;
import org.jaibo.api.errors.ExtensionError;
import org.jaibo.api.ServerListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Login and authentication
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

public final class Login extends Command implements ServerListener {
    private String[] nicknames;
    private String[] identities;
    private String information;

    private boolean loggedIn;

    private static int CurrentNicknameIndex = 0;
    private static int CurrentIdentityIndex = 0;

    private Object object;

    public Login() {
        this.setLoginData();
    }

    public Login(Object object) {
        this();

        this.object = object;
    }

    private void setLoginData() {
        this.nicknames = AIBO.Configuration.get("Login.nicknames").split(" ");
        this.identities = AIBO.Configuration.get("Login.identities").split(" ");
        this.information = AIBO.Configuration.get("aibo.whois_information");
    }

    @Override
    public void serverMessageReceived(String message) {
        this.checkAndExecute(message);
    }

    @Override
    public boolean check(String message) {
        boolean checkPassed = false;

        if (message.startsWith("NOTICE AUTH :*** Looking up your hostname")) {
            checkPassed = true;
        } else {
            Pattern p = Pattern.compile("^:(.*) 433 (.*):Nickname is already in use.$");

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                checkPassed = true;
            }
        }

        return checkPassed;
    }

    @Override
    protected void action() {
        String nickName = this.getNickName();

        this.object.getExtensionMessenger().getCommandSender().sendIrcCommand("nick", nickName);
        this.object.setCurrentNickName(nickName);

        if (!loggedIn) {
            this.object.getExtensionMessenger().getCommandSender().sendIrcCommand("user",
                    String.format("%s * ** : %s", this.getIdentity(), this.getInformation()));

            this.loggedIn = true;
        }
    }

    private String getNickName() {
        if (Login.CurrentNicknameIndex >= this.nicknames.length) {
            throw new ExtensionError(this.object.getExtensionName(), "All nicknames are already in use");
        } else {
            return this.nicknames[Login.CurrentNicknameIndex++];
        }
    }

    private String getIdentity() {
        if (Login.CurrentIdentityIndex >= this.identities.length) {
            throw new ExtensionError(this.object.getExtensionName(), "All identities are already in use");
        } else {
            return this.identities[Login.CurrentIdentityIndex++];
        }
    }

    private String getInformation() {
        return this.information;
    }
}
