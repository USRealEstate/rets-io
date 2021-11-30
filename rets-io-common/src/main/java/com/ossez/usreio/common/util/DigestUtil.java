/*
 * DigestUtil.java
 *
 * Created on October 4, 2002
 */
package com.ossez.usreio.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;

import java.util.*;


/**
 * @version 1.0
 */
public class DigestUtil {
    /**
     * log4j Category object
     */
    private final static Logger logger = LoggerFactory.getLogger(DigestUtil.class);

    /**
     * Create digest Authentication String
     */
    public static String digestAuthorization(String username, String password,
                                             String method, String uri, String value) {
        Map m = parseAuthenticate(value);
        String realm = (String) m.get("realm");
        String nonce = (String) m.get("nonce");
        String opaque = (String) m.get("opaque");
        String nc = "00000001";
        String cnonce = (String) m.get("cnonce");
        String qop = (String) m.get("qop");

        boolean isRFC2617 = (qop != null); // Recognizes differences between RFC2069 and RFC2617

        String digestResponse = Digest(username, realm, password, method, uri,
                nonce, nc, cnonce, qop);

        String digest = "Digest username=\"" + username + "\", " + "realm=\"" +
                realm + "\", " + "nonce=\"" + nonce + "\", " + "opaque=\"" +
                opaque + "\", " + "uri=\"" + uri + "\", " + "response=\"" +
                digestResponse + "\"";

        if (isRFC2617) {
            digest += (", qop=\"" + qop + "\", " + "cnonce=\"" + cnonce +
                    "\", " + "nc=\"" + nc + "\"");
        }

        return digest;
    }

    public static Map parseAuthenticate(String s) {
        String key = null;
        String value = null;
        int equalSign = 0;
        HashMap map = new HashMap();
        StringTokenizer commaTokenizer = new StringTokenizer(s, ",");

        while (commaTokenizer.hasMoreTokens()) {
            String token = commaTokenizer.nextToken();

            if ((equalSign = token.indexOf("=")) >= 0) {
                key = token.substring(0, equalSign).trim();
                value = removeQuotes(token.substring(equalSign + 1).trim());
                map.put(key, value);

                //cat.debug("parseAuthenticate:["+key+"]=["+value+"]");
            }
        }

        return map;
    }

    public static String Digest(String username, String realm, String password,
                                String method, String uri, String nonce, String nc, String cnonce,
                                String qop) {
        String digestResponse = null;
        boolean isRFC2617 = (qop != null); // Recognizes differences between RFC2069 and RFC2617

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.reset();

            String a1 = username + ":" + realm + ":" + password;

            String digestA1 = HexUtils.convert(md.digest(a1.getBytes()));
            logger.debug("evaluateMD5ResponseDigest: digestA1(" + a1 + ")=" +
                    digestA1);

            md.reset();

            String a2 = method + ":" + uri;
            String digestA2 = HexUtils.convert(md.digest(a2.getBytes()));
            logger.debug("evaluateMD5ResponseDigest: digestA2(" + a2 + ")=" +
                    digestA2);

            md.reset();

            String response = digestA1 + ":" + nonce;

            if (isRFC2617) {
                response += (":" + nc + ":" + cnonce + ":" + qop);
            }

            response += (":" + digestA2);

            digestResponse = HexUtils.convert(md.digest(response.getBytes()));
            logger.debug("evaluateMD5ResponseDigest: digestResponse(" + response +
                    ")=" + digestResponse);

            return digestResponse;
        } catch (Exception e) {
            //cat.error("evaluateMD5ResponseDigest: Exception occurred!",e);
            return digestResponse;
        }
    }

    public static String Digest(String value) {
        String digestResponse = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.reset();

            digestResponse = HexUtils.convert(md.digest(value.getBytes()));

            md.reset();
        } catch (Exception e) {
            // cat.error("evaluateMD5ResponseDigest: Exception occurred!",e);
        }

        return digestResponse;
    }

    /**
     * Removes the quotes on a string.
     */
    public static String removeQuotes(String quotedString) {
        quotedString = quotedString.replace('\"', ' ');

        return quotedString.trim();
    }
}
