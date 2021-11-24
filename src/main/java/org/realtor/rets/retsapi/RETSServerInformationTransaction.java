/**
 *        RETSServerInformationTransaction.java
 *
 *        @author        pobrien
 *        @version
 */
package org.realtor.rets.retsapi;


//import java.util.*;
import org.apache.log4j.*;


///////////////////////////////////////////////////////////////////////
public class RETSServerInformationTransaction extends RETSTransaction {
    static Category cat = Category.getInstance(RETSServerInformationTransaction.class);
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
