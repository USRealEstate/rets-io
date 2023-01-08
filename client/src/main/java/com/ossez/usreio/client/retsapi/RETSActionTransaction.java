package com.ossez.usreio.client.retsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *        RETSActionTransaction.java
 *
 *          This class is used to build an action transaction
 *
 *        @author        jbrush
 *        @version 1.0
 */
public class RETSActionTransaction extends RETSTransaction {
    private final static Logger logger = LoggerFactory.getLogger(RETSActionTransaction.class);

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
