/**
 *        RETSGetMetadataTransaction.java
 *
 *        @author        jbrush
 *        @version
 */
package org.realtor.rets.retsapi;


//import java.util.*;
import org.apache.log4j.*;


///////////////////////////////////////////////////////////////////////
public class RETSGetMetadataTransaction extends RETSTransaction {
    static Category cat = Category.getInstance(RETSGetMetadataTransaction.class);
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
