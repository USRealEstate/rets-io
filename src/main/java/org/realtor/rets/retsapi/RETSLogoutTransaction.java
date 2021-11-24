package org.realtor.rets.retsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *        Send a logout transaction to the server.
 *
 *        @author        jbrush
 *        @version 1.0
 */
public class RETSLogoutTransaction extends RETSTransaction {
    private final static Logger logger = LoggerFactory.getLogger(RETSLogoutTransaction.class);

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
