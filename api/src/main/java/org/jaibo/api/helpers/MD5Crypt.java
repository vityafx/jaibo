package org.jaibo.api.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 generator
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

public class MD5Crypt {
    public static String genMD5Hex(final String inputString) {
        String md5String = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputString.getBytes());

            byte[] digest = md.digest();

            md5String = convertByteToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return md5String;
    }

    private static String convertByteToHex(byte[] byteData) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}

