package IrcNetwork;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class describes irc events
 * Copyright (C) 2014  Victor Polevoy (vityatheboss@gmail.com)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public final class IrcEvent {
    private ArrayList<IrcUserMode> userModes = new ArrayList<IrcUserMode>();
    private IrcEventType eventType;
    private String user;
    private String host;
    private String channel;
    private String arguments;

    public IrcEvent() {

    }

    public static IrcEvent tryParse(String message) {
        return IrcEventParser.tryParseEvents(message);
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

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getArguments() {
        return arguments;
    }
}


class IrcEventParser {
    public static IrcEvent tryParseEvents(String message) {
        IrcEvent ircEvent = null;

        if(message != null) {
            ircEvent = tryParseJoinEvent(message);

            if (ircEvent == null) {
                ircEvent = tryParseTabClosedEvent(message);
            } if (ircEvent == null) {
                ircEvent = tryParseClientClosedEvent(message);
            } if (ircEvent == null) {
                ircEvent = tryParseKickedEvent(message);
            } if (ircEvent == null) {
                ircEvent = tryParseModeEvent(message);
            }
        }

        return ircEvent;
    }

    public static IrcEvent tryParseJoinEvent(String message) {
        IrcEvent ircEvent = null;

        if (message != null) {
            Pattern p = Pattern.compile("^(.*)!(.*) JOIN (.*)$");

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                ircEvent = new IrcEvent();

                ircEvent.setUser(matcher.group(1));
                ircEvent.setHost(matcher.group(2));
                ircEvent.setChannel(matcher.group(3));
                ircEvent.setEventType(IrcEventType.Join);
            }
        }

        return ircEvent;
    }

    public static IrcEvent tryParseTabClosedEvent(String message) {
        IrcEvent ircEvent = null;

        if (message != null) {
            Pattern p = Pattern.compile("^:(.*)!(.*) PART (.*) :(.*)$");

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                ircEvent = new IrcEvent();

                ircEvent.setUser(matcher.group(1));
                ircEvent.setHost(matcher.group(2));
                ircEvent.setChannel(matcher.group(3));
                ircEvent.setArguments(matcher.group(4));
                ircEvent.setEventType(IrcEventType.Part);
            }
        }

        return ircEvent;
    }

    public static IrcEvent tryParseClientClosedEvent(String message) {
        IrcEvent ircEvent = null;

        if (message != null) {
            Pattern p = Pattern.compile("^:(.*)!(.*) QUIT :(.*)$");

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                ircEvent = new IrcEvent();

                ircEvent.setUser(matcher.group(1));
                ircEvent.setHost(matcher.group(2));
                ircEvent.setArguments(matcher.group(4));
                ircEvent.setEventType(IrcEventType.Quit);
            }
        }

        return ircEvent;
    }

    public static IrcEvent tryParseKickedEvent(String message) {
        IrcEvent ircEvent = null;

        if (message != null) {
            Pattern p = Pattern.compile("^:(.*)!(.*) KICK (.*) (.*) :(.*)$");

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                ircEvent = new IrcEvent();

                ircEvent.setUser(matcher.group(1));
                ircEvent.setHost(matcher.group(2));
                ircEvent.setChannel(matcher.group(3));
                ircEvent.setArguments(matcher.group(4));
                ircEvent.setEventType(IrcEventType.Kick);
            }
        }

        return ircEvent;
    }

    public static IrcEvent tryParseModeEvent(String message) {
        IrcEvent ircEvent = null;

        if (message != null) {
            Pattern p = Pattern.compile("^:(.*)!(.*) MODE (.*) (.*) (.*)$");

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                ircEvent = new IrcEvent();

                ircEvent.setUser(matcher.group(1));
                ircEvent.setHost(matcher.group(2));
                ircEvent.setChannel(matcher.group(3));
                ircEvent.setUserModes(IrcEventParser.tryParseUserMode(matcher.group(4)));
                ircEvent.setArguments(matcher.group(5));
                ircEvent.setEventType(IrcEventType.Mode);
            }
        }

        return ircEvent;
    }

    private static ArrayList<IrcUserMode> tryParseUserMode(String modeString) {
        ArrayList<IrcUserMode> ircUserModes = new ArrayList<IrcUserMode>();

        if (modeString!= null) {
            if (modeString.contains("a")) {
                ircUserModes.add(IrcUserMode.Autooperator);
            } if (modeString.contains("g")) {
                ircUserModes.add(IrcUserMode.Autovoice);
            } if (modeString.contains("p")) {
                ircUserModes.add(IrcUserMode.Protect);
            } if (modeString.contains("t")) {
                ircUserModes.add(IrcUserMode.Topic);
            } if (modeString.contains("n")) {
                ircUserModes.add(IrcUserMode.Owner);
            } if (modeString.contains("m")) {
                ircUserModes.add(IrcUserMode.Master);
            } if (modeString.contains("v")) {
                ircUserModes.add(IrcUserMode.Voice);
            } if (modeString.contains("o")) {
                ircUserModes.add(IrcUserMode.Operator);
            } if (modeString.contains("k")) {
                ircUserModes.add(IrcUserMode.Known);
            } if (modeString.contains("b")) {
                ircUserModes.add(IrcUserMode.Banned);
            } if (modeString.contains("d")) {
                ircUserModes.add(IrcUserMode.Deopped);
            } if (modeString.contains("q")) {
                ircUserModes.add(IrcUserMode.Devoiced);
            }
        }

        return ircUserModes;
    }
}