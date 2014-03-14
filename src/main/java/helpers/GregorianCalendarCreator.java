package helpers;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates gregorian calendar object
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

public final class GregorianCalendarCreator {
    public static GregorianCalendar createFromCurrentDateByAppendingTimeString(String humanReadableTimeStamp) {
        GregorianCalendar calendar = null;

        if (humanReadableTimeStamp != null && !humanReadableTimeStamp.isEmpty()) {
            Pattern p = Pattern.compile("^(\\d+)([mhdw]{1})$", Pattern.CASE_INSENSITIVE);

            CharSequence sequence = humanReadableTimeStamp.trim().subSequence(0, humanReadableTimeStamp.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                calendar = new GregorianCalendar();

                int amount = Integer.parseInt(matcher.group(1));
                String typeOfAmount = matcher.group(2);

                if (typeOfAmount.equalsIgnoreCase("m")) {
                    calendar.add(GregorianCalendar.MINUTE, amount);
                } else if (typeOfAmount.equalsIgnoreCase("h")) {
                    calendar.add(GregorianCalendar.HOUR, amount);
                } else if (typeOfAmount.equalsIgnoreCase("d")) {
                    calendar.add(GregorianCalendar.DAY_OF_YEAR, amount);
                } else if (typeOfAmount.equalsIgnoreCase("w")) {
                    calendar.add(GregorianCalendar.WEEK_OF_YEAR, amount);
                }
            }
        }

        return calendar;
    }
}
