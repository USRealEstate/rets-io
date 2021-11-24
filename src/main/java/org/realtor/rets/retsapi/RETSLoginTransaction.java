package org.realtor.rets.retsapi;


//import org.apache.regexp.*;
import org.apache.log4j.*;

import java.net.*;

import java.util.*;


/**
 *        RETSLoginTransaction.java
 *
 *
 *        @author        jbrush
 *        @version 1.0
 */
public class RETSLoginTransaction extends RETSTransaction {
    /** log4j Object */
    static Category cat = Category.getInstance(RETSLoginTransaction.class);

    /**
     *  capablitiy list
     */
    private static final String[] capList = {
        "Login", // Login first because it might reset URL root
        "Action", "ChangePassword", "GetObject", "LoginComplete", "Logout",
        "Search", "GetMetadata", "Update", "ServerInformation"
    };
    String url = null;
    String version = null;

    public RETSLoginTransaction() {
        super();
        setRequestType("Login");
    }

    /** Sets the response body.  This method is called from RETSConnection.execute()
     *  @param body body of the response
     */
    public void setResponse(String body) {
        super.setResponse(body);

        setKeyValuePairs(body);

        setCapabilityUrls();
    }

    /**  Sets the username for this transaction
     *   @param username the user's login name
     */
    public void setUsername(String username) {
        setRequestVariable("username", username);
    }

    /** sets the User's password for this transaction.
     *  @param password password string
     */
    public void setPassword(String password) {
        setRequestVariable("password", password);
    }

    /** sets the URL for this transaction.
     *  @param url url string
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /** gets the URL for this transaction.
     *  @param url url string
     */
    public String getUrl() {
        return url;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    /**
     *
     */
    public void preprocess() {
        super.preprocess();

        // save the username and password
        transactionContext.put("username", getRequestVariable("username"));
        transactionContext.put("password", getRequestVariable("password"));
    }

    /**  Extracts the capabilitiesUrls out of the response body.
     *
     */
    void setCapabilityUrls() {
        Map respMap = getResponseMap();
        Set respKeySet = respMap.keySet();
        Iterator iter = null;
        String key = null;

        int capLength = capList.length;

        String urlRoot = getUrlRoot(url); // set root to current url root
        String capUrl = null;
        String qualifiedUrl = null;

        /* jump thru hoop because key might not be in proper mixed-case so we need to map it */
        for (int i = 0; i < capLength; i++) {
            iter = respKeySet.iterator();

            while (iter.hasNext()) {
                key = (String) iter.next();

                if (capList[i].equalsIgnoreCase(key)) {
                    capUrl = getResponseVariable(key);
                    qualifiedUrl = qualifyUrl(capUrl, urlRoot);

                    cat.debug(capList[i] + "=[" + qualifiedUrl + "]");
                    putCapabilityUrl(capList[i], qualifiedUrl);

                    if (capList[i].equalsIgnoreCase("Login")) // login may reset rootUrl
                     {
                        urlRoot = getUrlRoot(qualifiedUrl);
                    }

                    break;
                }
            }
        }
    }

    /**
     *        Makes sure url is fully qualified.
     */
    private String qualifyUrl(String url, String defaultRoot) {
        String root = getUrlRoot(url);
        String sep = "";

        if (root == null) {
            if (url.charAt(0) != '/') {
                sep = "/";
            }

            return defaultRoot + sep + url;
        } else {
            return url;
        }
    }

    String getUrlRoot(String myUrl) {
        try {
            URL url = new URL(myUrl);

            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();

            //String path = url.getPath();
            //String file = url.getFile();
            cat.debug("protocol = [" + protocol + "]");
            cat.debug("host = [" + host + "]");
            cat.debug("port = [" + port + "]");

            //cat.debug("path = ["+path+"]");
            //cat.debug("file = ["+file+"]");
            return protocol + "://" + host + ((port > 0) ? (":" + port) : "");
        } catch (MalformedURLException e) {
            cat.warn("getUrlRoot:MalformedURLException myUrl=\"" + myUrl +
                "\"");
        }

        return null;
    }
}
