package helperstests;

import helpers.GregorianCalendarHelper;
import junit.framework.TestCase;

import java.util.GregorianCalendar;

/**
 * Tests of GregorianCalendarHelper class
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

public final class GregorianCalendarCreatorTests extends TestCase {
    public void testCreatorAppendingByMinutes() {
        GregorianCalendar realCalendar = new GregorianCalendar();

        GregorianCalendar createdCalendar = GregorianCalendarHelper.createFromCurrentDateByAppendingTimeString("1m");
        realCalendar.add(GregorianCalendar.MINUTE, 1);

        assertEquals(createdCalendar.get(GregorianCalendar.MINUTE), realCalendar.get(GregorianCalendar.MINUTE));
    }

    public void testCreatorAppendingByHours() {
        GregorianCalendar realCalendar = new GregorianCalendar();

        GregorianCalendar createdCalendar = GregorianCalendarHelper.createFromCurrentDateByAppendingTimeString("1h");
        realCalendar.add(GregorianCalendar.HOUR, 1);

        assertEquals(createdCalendar.get(GregorianCalendar.HOUR), realCalendar.get(GregorianCalendar.HOUR));
    }

    public void testCreatorAppendingByDays() {
        GregorianCalendar realCalendar = new GregorianCalendar();

        GregorianCalendar createdCalendar = GregorianCalendarHelper.createFromCurrentDateByAppendingTimeString("2d");
        realCalendar.add(GregorianCalendar.DAY_OF_YEAR, 2);

        assertEquals(createdCalendar.get(GregorianCalendar.DAY_OF_YEAR), realCalendar.get(GregorianCalendar.DAY_OF_YEAR));
    }

    public void testCreatorAppendingByWeeks() {
        GregorianCalendar realCalendar = new GregorianCalendar();

        GregorianCalendar createdCalendar = GregorianCalendarHelper.createFromCurrentDateByAppendingTimeString("1w");
        realCalendar.add(GregorianCalendar.HOUR, 7 * 24);

        assertEquals(createdCalendar.get(GregorianCalendar.DAY_OF_YEAR), realCalendar.get(GregorianCalendar.DAY_OF_YEAR));
    }
}