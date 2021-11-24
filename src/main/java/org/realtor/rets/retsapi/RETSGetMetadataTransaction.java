/**
 *        RETSGetMetadataTransaction.java
 *
 *        @author        jbrush
 *        @version
 */
package org.realtor.rets.retsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


///////////////////////////////////////////////////////////////////////
public class RETSGetMetadataTransaction extends RETSTransaction {
    private final static Logger logger = LoggerFactory.getLogger(RETSConnection.class);
    String version = null;

    /**
     * constructor
     */
    public RETSGetMetadataTransaction() {
        super();
        setRequestType("GetMetadata");
    }

    public void setType(String str) {
        setRequestVariable("Type", str);
    }

    public void setId(String str) {
        setRequestVariable("ID", str);
    }

    public void setFormat(String str) {
        setRequestVariable("Format", str);
    }

    //	public void setResource(String str) {
    //		setRequestVariable("resource", str);
    //	}
    //	public void setResourceClass(String str) {
    //		setRequestVariable("resourceClass", str);
    //	}
    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
