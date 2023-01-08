package com.ossez.usreio.common.util;

import java.io.*;


public final class HexUtils {
    /**
     * Convert a String of hexadecimal digits into the corresponding
     * byte array by encoding each two hexadecimal digits as a byte.
     *
     * @param digits Hexadecimal digits representation
     *
     * @exception IllegalArgumentException if an invalid hexadecimal digit
     *  is found, or the input string contains an odd number of hexadecimal
     *  digits
     */
    public static byte[] convert(String digits) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int i = 0; i < digits.length(); i += 2) {
            char c1 = digits.charAt(i);

            if ((i + 1) >= digits.length()) {
                throw new IllegalArgumentException("Must be a hex number");
            }

            char c2 = digits.charAt(i + 1);
            byte b = 0;

            if ((c1 >= '0') && (c1 <= '9')) {
                b += ((c1 - '0') * 16);
            } else if ((c1 >= 'a') && (c1 <= 'f')) {
                b += ((c1 - 'a' + 10) * 16);
            } else if ((c1 >= 'A') && (c1 <= 'F')) {
                b += ((c1 - 'A' + 10) * 16);
            } else {
                throw new IllegalArgumentException("Must be a hex number");
            }

            if ((c2 >= '0') && (c2 <= '9')) {
                b += (c2 - '0');
            } else if ((c2 >= 'a') && (c2 <= 'f')) {
                b += (c2 - 'a' + 10);
            } else if ((c2 >= 'A') && (c2 <= 'F')) {
                b += (c2 - 'A' + 10);
            } else {
                throw new IllegalArgumentException("Must be a hex number");
            }

            baos.write(b);
        }

        return (baos.toByteArray());
    }

    /**
     * Convert a byte array into a printable format containing a
     * String of hexadecimal digit characters (two per byte).
     *
     * @param bytes Byte array representation
     */
    public static String convert(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            sb.append(convertDigit((int) (bytes[i] >> 4)));
            sb.append(convertDigit((int) (bytes[i] & 0x0f)));
        }

        return (sb.toString());
    }

    /**
     * [Private] Convert the specified value (0 .. 15) to the corresponding
     * hexadecimal digit.
     *
     * @param value Value to be converted
     */
    private static char convertDigit(int value) {
        value &= 0x0f;

        if (value >= 10) {
            return ((char) (value - 10 + 'a'));
        } else {
            return ((char) (value + '0'));
        }
    }
}
