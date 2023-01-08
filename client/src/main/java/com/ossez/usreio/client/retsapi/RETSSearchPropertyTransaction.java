package com.ossez.usreio.client.retsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * RETSSearchPropertyBatchTransaction.java
 *
 * @author jbrush
 * @version 1.0
 */
public class RETSSearchPropertyTransaction extends RETSSearchTransaction {
    private final static Logger logger = LoggerFactory.getLogger(RETSSearchPropertyTransaction.class);

    public RETSSearchPropertyTransaction() {
        super();
        setSearchType("Property");
    }
}
