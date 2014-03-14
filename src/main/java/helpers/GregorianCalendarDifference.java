package helpers;

import java.util.GregorianCalendar;

/**
 * Gregorian calendar difference
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

public final class GregorianCalendarDifference {
    public static String GetDifferenceAsHumanReadableString(GregorianCalendar calendar1, GregorianCalendar calendar2) {
        long difference = calendar1.getTimeInMillis() - calendar2.getTimeInMillis();

        long secondInMillis = 1000;
        long minuteInMillis = secondInMillis * 60;
        long hourInMillis = minuteInMillis * 60;
        long dayInMillis = hourInMillis * 24;

        long elapsedDays = difference / dayInMillis;
        difference = difference % dayInMillis;
        long elapsedHours = difference / hourInMillis;
        difference = difference % hourInMillis;
        long elapsedMinutes = difference / minuteInMillis;
        difference = difference % minuteInMillis;
        long elapsedSeconds = difference / secondInMillis;

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

        return lastGameStringBuilder.toString();
    }
}
