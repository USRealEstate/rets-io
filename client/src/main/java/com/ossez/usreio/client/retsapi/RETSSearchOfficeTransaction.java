package com.ossez.usreio.client.retsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *        RETSSearchOfficeTransaction.java
 *      Performs a getOffice Transaction
 *
 *        @author        jbrush
 *        @version 1.0
 */
public class RETSSearchOfficeTransaction extends RETSSearchTransaction {
    private final static Logger logger = LoggerFactory.getLogger(RETSSearchOfficeTransaction.class);

    /**  Creates new a RETSSearchOfficeTransaction
     *
     */
    public RETSSearchOfficeTransaction() {
        super();
        setSearchType("Office");

        setSearchClass("Office");
//        setSearchClass("OFF");
    }
}
