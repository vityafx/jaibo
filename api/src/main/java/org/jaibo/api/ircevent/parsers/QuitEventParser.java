package org.jaibo.api.ircevent.parsers;

import org.jaibo.api.IrcEvent;
import org.jaibo.api.ircevent.IrcEventParser;
import org.jaibo.api.ircevent.IrcEventType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Quit irc event parser
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

public final class QuitEventParser implements IrcEventParser {
    @Override
    public IrcEvent tryParse(String message) {
        IrcEvent ircEvent = null;

        if (message != null) {
            Pattern p = Pattern.compile("^:(.*)!(.*) QUIT :(.*)$");

            CharSequence sequence = message.subSequence(0, message.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                ircEvent = new IrcEvent();

                ircEvent.setUser(matcher.group(1));
                ircEvent.setHost(matcher.group(2));
                ircEvent.setArgument("QuitMessage", matcher.group(3));
                ircEvent.setEventType(IrcEventType.Quit);
            }
        }

        return ircEvent;
    }
}