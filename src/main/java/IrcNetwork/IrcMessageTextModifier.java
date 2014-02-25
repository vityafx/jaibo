package IrcNetwork;

import java.util.HashMap;

/**
 * Irc message visual text modifier
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


public final class IrcMessageTextModifier {
    public final static String BOLD = "\002";
    public final static String INVERSED = "\022";
    public final static String UNDERLINE = "\037";

    public final static String COLOREDPREFIX = "\u0003";

    public final static HashMap<IrcTextColor, String> IrcTextColors;
    static
    {
        IrcTextColors = new HashMap<IrcTextColor, String>();

        IrcTextColors.put(IrcTextColor.White, "00");
        IrcTextColors.put(IrcTextColor.Black, "01");
        IrcTextColors.put(IrcTextColor.DarkBlue, "02");
        IrcTextColors.put(IrcTextColor.Green, "03");
        IrcTextColors.put(IrcTextColor.Red, "04");
        IrcTextColors.put(IrcTextColor.Brown, "05");
        IrcTextColors.put(IrcTextColor.DarkPurple, "06");
        IrcTextColors.put(IrcTextColor.Orange, "07");
        IrcTextColors.put(IrcTextColor.Yellow, "08");
        IrcTextColors.put(IrcTextColor.LightGreen, "09");
        IrcTextColors.put(IrcTextColor.SeaColor, "19");
        IrcTextColors.put(IrcTextColor.LightBlue, "11");
        IrcTextColors.put(IrcTextColor.Blue, "12");
        IrcTextColors.put(IrcTextColor.Pink, "13");
        IrcTextColors.put(IrcTextColor.DarkGrey, "14");
        IrcTextColors.put(IrcTextColor.Grey, "15");
    }

    public static String makeBold(String text) {
        return String.format("%s%s%s", BOLD, text, BOLD);
    }

    public static String makeInversed(String text) {
        return String.format("%s%s%s", INVERSED, text, INVERSED);

    }
    public static String makeUnderlined(String text) {
        return String.format("%s%s%s", UNDERLINE, text, UNDERLINE);

    }

    public static String makeColoured(String text, IrcTextColor color) {
        String colorCode = String.format("%s%s", COLOREDPREFIX, IrcTextColors.get(color));

        return String.format("%s%s%s", colorCode, text, COLOREDPREFIX);
    }

    public static String makeBackgroundColoured(String text, IrcTextColor color) {
        String colorCode = String.format(
                "%s%s,%s",
                COLOREDPREFIX,
                IrcTextColors.get(IrcTextColor.Black),
                IrcTextColors.get(color));

        return String.format("%s%s%s", colorCode, text, COLOREDPREFIX);
    }

    public static String makeMixedColoured(String text, IrcTextColor backgroundColor, IrcTextColor foregroundColor) {
        String foregroundColorString = IrcTextColors.get(foregroundColor);
        String backgroundColorString = IrcTextColors.get(backgroundColor);

        String colorCode = String.format("%s%s,%s", COLOREDPREFIX, foregroundColorString, backgroundColorString);

        return String.format("%s%s%s", colorCode, text, COLOREDPREFIX);
    }
}
