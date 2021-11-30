/**
 *        RETSServerInformationTransaction.java
 *
 *        @author        pobrien
 *        @version
 */
package com.ossez.usreio.client.retsapi;


//import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


///////////////////////////////////////////////////////////////////////
public class RETSServerInformationTransaction extends RETSTransaction {
    private final static Logger logger = LoggerFactory.getLogger(RETSServerInformationTransaction.class);
    String version = null;

    /**
     * constructor
     */
    public RETSServerInformationTransaction() {
        super();
        setRequestType("ServerInformation");
    }

    public void setResource(String str) {
        setRequestVariable("Resource", str);
    }

    public void setInfoClass(String str) {
        setRequestVariable("Class", str);
    }

    public void setStandardNames(String str) {
        setRequestVariable("StandardNames", str);
    }


    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

}
