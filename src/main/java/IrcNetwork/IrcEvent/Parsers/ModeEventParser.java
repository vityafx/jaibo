package IrcNetwork.IrcEvent.Parsers;

import IrcNetwork.IrcEvent.IrcEvent;
import IrcNetwork.IrcEvent.IrcEventParser;
import IrcNetwork.IrcEvent.IrcEventType;
import IrcNetwork.IrcUserMode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mode irc event parser.
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

public final class ModeEventParser implements IrcEventParser {
    @Override
    public IrcEvent tryParse(String message) {
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
                ircEvent.setUserModes(ModeEventParser.tryParseUserMode(matcher.group(4)));
                ircEvent.setArgument("User", matcher.group(5));
                ircEvent.setEventType(IrcEventType.Mode);
            }
        }

        return ircEvent;
    }

    public static ArrayList<IrcUserMode> tryParseUserMode(String modeString) {
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