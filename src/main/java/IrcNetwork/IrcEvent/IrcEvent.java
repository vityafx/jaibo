package IrcNetwork.IrcEvent;

import IrcNetwork.IrcEvent.Parsers.*;
import IrcNetwork.IrcUserMode;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class describes irc events
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

public final class IrcEvent {
    private static final IrcEventParser[] EventParsers = {
            new KickEventParser(),
            new ModeEventParser(),
            new NickEventParser(),
            new QuitEventParser(),
            new TabCloseEventParser(),
            new JoinEventParser()
    };

    private ArrayList<IrcUserMode> userModes = new ArrayList<IrcUserMode>();
    private IrcEventType eventType;
    private String user;
    private String host;
    private String channel;
    private HashMap<String, String> arguments = new HashMap<String, String>();

    public IrcEvent() {

    }

    public static IrcEvent tryParse(String message) {
        IrcEvent ircEvent = null;

        for(IrcEventParser parser : EventParsers) {
            ircEvent = parser.tryParse(message);

            if (ircEvent != null) {
                break;
            }
        }

        return ircEvent;
    }


    public void setUserModes(ArrayList<IrcUserMode> userModes) {
        this.userModes = userModes;
    }

    public void addUserMode(IrcUserMode userMode) {
        this.userModes.add(userMode);
    }

    public ArrayList<IrcUserMode> getUserModes() {
        return userModes;
    }

    public void setEventType(IrcEventType eventType) {
        this.eventType = eventType;
    }

    public IrcEventType getEventType() {
        return eventType;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setArguments(HashMap<String, String> arguments) {
        this.arguments = arguments;
    }

    public void setArgument(String key, String argument) {
        if (this.arguments != null) {
            this.arguments.put(key, argument);
        }
    }

    public String getArgument(String key) {
        if (this.arguments != null) {
            return this.arguments.get(key);
        }

        return null;
    }
}