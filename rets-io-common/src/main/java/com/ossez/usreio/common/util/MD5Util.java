/*
 * MD5Util.java
 *
 * Created on November 21, 2001, 9:54 AM
 */
package com.ossez.usreio.common.util;

import java.security.*;


/**
 *
 * @version 1.0
 */
public class MD5Util {
    /** Creates new MD5Util */
    public MD5Util() {
    }

    /** returns MD5 HEX value for a given string
     *  @param source string to convert
     */
    public static String getDigestAsHexString(String source) {
        return getDigestAsHexString(source.getBytes());
    }

    /** returns MD5 HEX value for a given a byte array
     *  @param source byte array to convert
     */
    public static String getDigestAsHexString(byte[] source) {
        String retStr = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();

            byte[] digestBytes = md.digest(source);
            retStr = convertToHex(digestBytes);
        } catch (java.security.NoSuchAlgorithmException nsa) {
            nsa.printStackTrace();
        }

        return retStr;
    }

    /** Returns the HEX representation of a byte array
     *  @param source byte array to convert to HEX
     */
    private static String convertToHex(byte[] source) {
        String text;
        int j;
        int i;
        text = "";

        for (i = 0; i < 16; i++) {
            j = source[i];
            j = j & 255;

            if (j <= 15) {
                text += "0";
            }

            text += Integer.toHexString(j);
        }

        return text;
    }
}
