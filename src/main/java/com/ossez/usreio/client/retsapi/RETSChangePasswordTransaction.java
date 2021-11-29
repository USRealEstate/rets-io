package com.ossez.usreio.client.retsapi;


import org.realtor.rets.util.DesCrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;


/**
 * Send a Change Password transaction to the server.
 *
 * @author jbrush
 * @version 1.0
 */
public class RETSChangePasswordTransaction extends RETSTransaction {
    private final static Logger logger = LoggerFactory.getLogger(RETSActionTransaction.class);

    private String oldPassword = null;
    private String newPassword = null;
    private String newPassword2 = null;
    private String encrypted = null;
    private String decrypted = null;
    private String username = null;

    /**
     * Create a new RETSChangePasswordTransaction
     */
    public RETSChangePasswordTransaction() {
        super();
        setRequestType("ChangePassword");
    }

    /**
     * Sets the username
     *
     * @param username name user signs in with
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the old password
     *
     * @param passwd users password to be changed
     */
    public void setOldPassword(String passwd) {
        this.oldPassword = passwd;
    }

    /**
     * Sets the new password value
     *
     * @param passwd new password
     */
    public void setNewPassword(String passwd) {
        this.newPassword = passwd;
    }

    /**
     * Sets the new password confirm value
     *
     * @param passwd new password
     */
    public void setNewPassword2(String passwd) {
        this.newPassword2 = passwd;
    }

    /**
     * process the transaction
     */
    public void preprocess() {
        String errMsg = null;

        super.preprocess();

        setUsername((String) transactionContext.get("username"));

        logger.debug("username=" + username);
        logger.debug("oldPassword=" + oldPassword);
        logger.debug("newPassword=" + newPassword);

        //cat.debug("newPassword2="+newPassword2);

        /*if (oldPassword == null
        || !oldPassword.equals(transactionContext.get("password"))) {
                errMsg = "Old Password does not match.";
        }
        else if ((newPassword1 == null || newPassword2 == null)
                || (!newPassword1.equals(newPassword2))) {
                errMsg = "New Passwords do not match.";
        }*/

        //else {
        String pwd = encryptPWD();

        //cat.debug("PWD:"+pwd);
        setRequestVariable("PWD", pwd);

        //}
        if (errMsg != null) {
            logger.warn(errMsg);

            setResponseStatus("20513"); // Miscellaneous error
            setResponseStatusText(errMsg);
            setResponse(errMsg);

            errMsg = null;
        }
    }

    public void postprocess() {
        transactionContext.put("password", newPassword);
    }

    private String encryptPWD() {
        byte[] key = makeKey();
        String source = newPassword + ":" + username;

        return DES(key, source);
    }

    private String DES(byte[] keyBytes, String source) {
        try {
            // Create encrypter/decrypter class
            DesCrypter crypter = new DesCrypter(keyBytes);

            // Encrypt
            encrypted = crypter.encrypt(source);

            // Decrypt
            decrypted = crypter.decrypt(encrypted);

            return encrypted;
        } catch (Exception e) {
        }

        return null;
    }

    private byte[] makeKey() {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        md.reset();

        // trim to 8 bytes
        byte[] key = new byte[8];
        System.arraycopy(md.digest((oldPassword + username).toUpperCase()
                .getBytes()), 0, key, 0, 8);

        return key;
    }

    /////////////////////////////////////////////////
    public static void main(String[] args) {
        RETSChangePasswordTransaction t = new RETSChangePasswordTransaction();

        t.setUsername(args[0]);
        t.setOldPassword(args[1]);
        t.setNewPassword(args[2]);

        //t.setNewPassword2(args[2]);
        t.preprocess();

        System.out.println("encrypted=" + t.encrypted);
        System.out.println("decrypted=" + t.decrypted);
    }
}
