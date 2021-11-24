package org.realtor.rets.retsapi;

import org.apache.log4j.*;


/**
 *        Send a logout transaction to the server.
 *
 *        @author        jbrush
 *        @version 1.0
 */
public class RETSLogoutTransaction extends RETSTransaction {
    static Category cat = Category.getInstance(RETSLogoutTransaction.class);

    /** Create a new RETSLogoutTransaction */
    public RETSLogoutTransaction() {
        super();
        setRequestType("Logout");
    }

    /**  Sets the response body.  Called from RETSConnection.execute()
     *   after the logout transaction is executed.
     *
     *   @param body Body of the response to the RETSLogoutTransaction.
     */
    public void setResponse(String body) {
        super.setResponse(body);

        setKeyValuePairs(body);
    }
}
