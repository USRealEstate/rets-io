// $Header: /usr/local/cvsroot/rets/commons/src/main/java/org/realtor/rets/util/RETSConfigurator.java,v 1.2 2003/12/04 15:27:03 rsegelman Exp $
package com.ossez.usreio.common.rets;


/**
 * RETSConfigurator
 * Singleton to limit number of times BasicConfigurator.configure is called.
 */
public class RetsConfigurator {
    static boolean configured = false;
    private String serverUrl;
    private String serverUsername;
    private String serverPassword;
    private RetsVersion retsVersion = RetsVersion.DEFAULT;

    public RetsConfigurator() {
    }

    /**
     * calls <code>BasicConfigurator.configure()</code> only once
     */
    static public void configure() {
        if (!configured) {
//            BasicConfigurator.configure();
            configured = true;
        }
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServerUsername() {
        return serverUsername;
    }

    public void setServerUsername(String serverUsername) {
        this.serverUsername = serverUsername;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public RetsVersion getRetsVersion() {
        return retsVersion;
    }

    public void setRetsVersion(RetsVersion retsVersion) {
        this.retsVersion = retsVersion;
    }
}
