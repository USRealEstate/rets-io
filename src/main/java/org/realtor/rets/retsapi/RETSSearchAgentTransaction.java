package org.realtor.rets.retsapi;

import org.apache.log4j.*;


/**
 *        RETSSearchAgentTransaction.java
 *      Search for agents
 *
 *        @author        jbrush
 *        @version 1.0
 */
public class RETSSearchAgentTransaction extends RETSSearchTransaction {
    static Category cat = Category.getInstance(RETSSearchAgentTransaction.class);

    /**create a new RETSSearchAgentTransaction*/
    public RETSSearchAgentTransaction() {
        super();
        setSearchType("Agent");
        setSearchClass("Agent");
    }

    /**  Search by last name, pass in the lastname of a user
     *   as the "query" argument.
     *   @param searchByLastName lastname of the user to search.
     */
    public void setSearchByLastname(String searchByLastname) {
        // convert to DMQL
        setSearchQuery("(LastName=" + searchByLastname + ")");
    }
}
