package com.ossez.usreio.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 */
public class DesCrypter {

    private final static Logger logger = LoggerFactory.getLogger(DesCrypter.class);
    Cipher ecipher;
    Cipher dcipher;

    //String transform = "DES";
    String transform = "DES/ECB/PKCS5Padding";

    public DesCrypter(byte[] keyBytes) {
        SecretKey key = new SecretKeySpec(keyBytes, "DES");

        try {
            ecipher = Cipher.getInstance(transform);
            ecipher.init(Cipher.ENCRYPT_MODE, key);

            dcipher = Cipher.getInstance(transform);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (javax.crypto.NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (java.security.InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string


            return Base64.getEncoder().encodeToString(enc);
        } catch (javax.crypto.BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decrypt(String str) {
        try {
            // Decode base64 to get bytes
            byte[] dec = Base64.getDecoder().decode(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
