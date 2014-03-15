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

public final class GregorianCalendarHelper {
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

    public static String GetDifferenceAsHumanReadableString(GregorianCalendar calendar1, GregorianCalendar calendar2) {
        long difference = calendar1.getTimeInMillis() - calendar2.getTimeInMillis();

        return GetHumanReadableDateAsString(difference);
    }

    public static String GetHumanReadableDateAsString(long timeInMillis) {
        long secondInMillis = 1000;
        long minuteInMillis = secondInMillis * 60;
        long hourInMillis = minuteInMillis * 60;
        long dayInMillis = hourInMillis * 24;

        long elapsedDays = timeInMillis / dayInMillis;
        timeInMillis = timeInMillis % dayInMillis;
        long elapsedHours = timeInMillis / hourInMillis;
        timeInMillis = timeInMillis % hourInMillis;
        long elapsedMinutes = timeInMillis / minuteInMillis;
        timeInMillis = timeInMillis % minuteInMillis;
        long elapsedSeconds = timeInMillis / secondInMillis;

        StringBuilder lastGameStringBuilder = new StringBuilder();

        if (elapsedDays > 0) {
            lastGameStringBuilder.append(String.format("%d days ", elapsedDays));
        }

        if (elapsedHours > 0) {
            lastGameStringBuilder.append(String.format("%d hours ", elapsedHours));
        }

        if (elapsedMinutes > 0) {
            lastGameStringBuilder.append(String.format("%d minutes ", elapsedMinutes));
        }

        if (elapsedSeconds > 0) {
            lastGameStringBuilder.append(String.format("%d seconds ", elapsedSeconds));
        }

        return lastGameStringBuilder.toString().trim();
    }

    public static String GetHumanReadableDateFromString(String dateString) {
        String resultString = null;

        if (dateString != null && !dateString.isEmpty()) {
            Pattern p = Pattern.compile("^(\\d+)([mhdw]{1})$", Pattern.CASE_INSENSITIVE);

            CharSequence sequence = dateString.trim().subSequence(0, dateString.length());
            Matcher matcher = p.matcher(sequence);

            if (matcher.matches()) {
                int amount = Integer.parseInt(matcher.group(1));
                String typeOfAmount = matcher.group(2);

                if (typeOfAmount.equalsIgnoreCase("m")) {
                    resultString = String.format("%d minutes", amount);
                } else if (typeOfAmount.equalsIgnoreCase("h")) {
                    resultString = String.format("%d hours", amount);
                } else if (typeOfAmount.equalsIgnoreCase("d")) {
                    resultString = String.format("%d days", amount);
                } else if (typeOfAmount.equalsIgnoreCase("w")) {
                    resultString = String.format("%d weeks", amount);
                }
            }
        }

        return resultString;
    }
}
