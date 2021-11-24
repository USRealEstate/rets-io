package org.realtor.rets.retsapi;

import org.apache.log4j.*;


/**
 *        RETSActionTransaction.java
 *
 *          This class is used to build an action transaction
 *
 *        @author        jbrush
 *        @version 1.0
 */
public class RETSActionTransaction extends RETSTransaction {
    static Category cat = Category.getInstance(RETSConnection.class);

    /**
     *  Constructor
     */
    public RETSActionTransaction() {
        super();
        setRequestType("Action");
    }

    /**
     *   Sets the reponse body.
     *
     *@param body text of the response
     *
     */
    public void setResponse(String body) {
        super.setResponse(body);
    }
}
