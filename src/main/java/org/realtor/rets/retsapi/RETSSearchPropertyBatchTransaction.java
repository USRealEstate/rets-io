package org.realtor.rets.retsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *        RETSSearchPropertyBatchTransaction.java
 *
 *        @author        jbrush
 *        @version 1.0
 */
public class RETSSearchPropertyBatchTransaction extends RETSSearchTransaction {
    private final static Logger logger = LoggerFactory.getLogger(RETSSearchPropertyBatchTransaction.class);

    public RETSSearchPropertyBatchTransaction() {
        super();
        setSearchType("Property");
        setSearchClass("RES");
    }

    public void setSearchByListingAgent(String agent) {
        // convert to DMQL
        setSearchQuery("(AgentID=" + agent + ")");
    }
}
