package org.realtor.rets.retsapi;

import org.apache.log4j.*;


/**
 *        RETSSearchPropertyBatchTransaction.java
 *
 *        @author        jbrush
 *        @version 1.0
 */
public class RETSSearchPropertyBatchTransaction extends RETSSearchTransaction {
    static Category cat = Category.getInstance(RETSSearchPropertyBatchTransaction.class);

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
