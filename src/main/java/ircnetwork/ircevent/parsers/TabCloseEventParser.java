package ircnetwork.ircevent.parsers;

import ircnetwork.ircevent.IrcEvent;
import ircnetwork.ircevent.IrcEventParser;
import ircnetwork.ircevent.IrcEventType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tab close irc event parser
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

public final class TabCloseEventParser implements IrcEventParser {
    @Override
    public IrcEvent tryParse(String message) {
        IrcEvent ircEvent = null;

        ircEvent = defaultPatternParser(message);

        if (ircEvent == null) {
            ircEvent = simplePatternParser(message);
        }

        return ircEvent;
    }

    private IrcEvent simplePatternParser(String message) {
        IrcEvent ircEvent = null;

        if (message != null) {
            Pattern p = Pattern.compile("^:(.*)!(.*) PART (.*)$");

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                ircEvent = new IrcEvent();

                ircEvent.setUser(matcher.group(1));
                ircEvent.setHost(matcher.group(2));
                ircEvent.setChannel(matcher.group(3));
                ircEvent.setArgument("CloseMessage", "");
                ircEvent.setEventType(IrcEventType.Part);
            }
        }

        return ircEvent;
    }

    private IrcEvent defaultPatternParser(String message) {
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
                ircEvent.setArgument("CloseMessage", matcher.group(4));
                ircEvent.setEventType(IrcEventType.Part);
            }
        }

        return ircEvent;
    }
}