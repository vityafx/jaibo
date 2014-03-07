package ircnetwork;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * File describes a single irc user
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

public final class IrcUser {
    private String nick;
    private String name;
    private String host;
    private String rights;
    private String lastLogin;
    private Map<String, String> information;

    public IrcUser() {

    }

    public IrcUser(String nick,
                    String name,
                    String host,
                    String rights,
                    String lastLogin,
                    Map<String, String> information) {
        this.setNick(nick);
        this.setName(name);
        this.setHost(host);
        this.setRights(rights);
        this.setLastLogin(lastLogin);
        this.setInformation(information);
    }

    public IrcUser(String nick, String host) {
        this.setNick(nick);
        this.setHost(host);
    }

    public static IrcUser tryParse(String user) {
        IrcUser ircUser = null;

        Pattern p = Pattern.compile("^[:](.*)!(.*)@(.*)$");

        CharSequence sequence = user.subSequence(0, user.length());
        Matcher matcher = p.matcher(sequence);

        if (matcher.matches()) {
            ircUser = new IrcUser(  matcher.group(1),
                                    matcher.group(2),
                                    matcher.group(3),
                                    null,
                                    null,
                                    null
            );
        }

        return ircUser;
    }

    public static IrcUser tryParseFromIrcMessage(String message) {
        IrcUser ircUser = null;

        Pattern p = Pattern.compile("^[:](.*)!(.*)@([\\.|\\w+]*) (.*)$");

        CharSequence sequence = message.subSequence(0, message.length());
        Matcher matcher = p.matcher(sequence);

        if (matcher.matches()) {
            ircUser = new IrcUser(  matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    null,
                    null,
                    null
            );
        }

        return ircUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Map<String, String> getInformation() {
        return information;
    }

    public void setInformation(Map<String, String> information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
