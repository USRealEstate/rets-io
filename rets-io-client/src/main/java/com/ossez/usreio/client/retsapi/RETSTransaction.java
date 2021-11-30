/**
 * RETSTransaction.java
 *
 * @author jbrush
 * @version
 */
package com.ossez.usreio.client.retsapi;

import com.ossez.usreio.common.util.RETSRequestResponse;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


///////////////////////////////////////////////////////////////////////

public class RETSTransaction extends RETSRequestResponse {
    private final static Logger logger = LoggerFactory.getLogger(RETSConnection.class);

    private static final String STATUS = "status";
    private static final String STATUSTEXT = "statusText";
    private static final String BODY = "body";
    private static final String REQUESTTYPE = "requestType";
    protected static final String RESOURCE = "Resource";
    protected static final String CLASS_NAME = "ClassName";
    protected HashMap transactionContext = null;
    private HashMap capabilityUrls = null;
    protected HashMap responseHeaderMap = null;
    InputStream responseStream = null;
    RE firstStatusRE = null;
    RE secondStatusRE = null;

    /**
     * Holds value of property compressionFormat.
     */
    private String compressionFormat = null;

    public RETSTransaction() {
        super();

        try {
            // TODO: RE's should be precompiled
            firstStatusRE = new RE("<RETS\\s?ReplyCode=\"(.*?)\"\\s?ReplyText=\"(.*?)\"");
            secondStatusRE = new RE("<RETS-STATUS\\s?ReplyCode=\"(.*?)\"\\s?ReplyText=\"(.*?)\"");
        } catch (RESyntaxException e) {
            logger.error("Error compiling REs", e);
        }
    }

    public void setResource(String resource) {
        setRequestVariable(RESOURCE, resource);
    }

    public String getResource() {
        return getRequestVariable(RESOURCE);
    }

    public void setClassName(String className) {
        setRequestVariable(CLASS_NAME, className);
    }

    public String getClassName() {
        return getRequestVariable(CLASS_NAME);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setRequestType(String type) {
        setRequestVariable(REQUESTTYPE, type);
    }

    public String getRequestType() {
        return getRequestVariable(REQUESTTYPE);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setRequest(String body) {
        setRequestVariable(BODY, body);
    }

    public String getRequest() {
        return getRequestVariable(BODY);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setResponse(String body) {
        setResponseVariable(BODY, body);

        // extract values
        // cat.debug("looking for <RETS tag");
        if ((body == null) || !firstStatusRE.match(body)) {
            return;
        }

        setResponseStatus(firstStatusRE.getParen(1));
        setResponseStatusText(firstStatusRE.getParen(2));

        // extract RETS-STATUS values is any
        if (secondStatusRE.match(body)) {
            setResponseStatus(secondStatusRE.getParen(1));
            setResponseStatusText(secondStatusRE.getParen(2));
        }
    }

    public String getResponse() {
        return getResponseVariable(BODY);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setResponseStream(InputStream is) {
        responseStream = is;
    }

    public InputStream getResponseStream() {
        return responseStream;
    }

    ///////////////////////////////////////////////////////////////////////
    public void setResponseStatus(String status) {
        setResponseVariable(STATUS, status);
    }

    public String getResponseStatus() {
        return getResponseVariable(STATUS);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setResponseStatusText(String statusText) {
        setResponseVariable(STATUSTEXT, statusText);
    }

    public String getResponseStatusText() {
        return getResponseVariable(STATUSTEXT);
    }

    ///////////////////////////////////////////////////////////////////////
    public String getUrl() {
        String url = getCapabilityUrl(getRequestType());

        logger.debug("getUrl():" + getRequestType() + " url:" + url);

        return url;
    }

    ///////////////////////////////////////////////////////////////////////
    void setKeyValuePairs(String str) {
        if (str == null) {
            return;
        }

        StringTokenizer lineTokenizer = new StringTokenizer(str, "\r\n");

        while (lineTokenizer.hasMoreTokens()) {
            String line = lineTokenizer.nextToken();

            // if tag, ignore it
            if (line.charAt(0) != '<') {
                int equalSign = line.indexOf("=");

                if (equalSign >= 0) {
                    setResponseVariable(line.substring(0, equalSign).trim(),
                            line.substring(equalSign + 1).trim());
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////
    void putCapabilityUrl(String key, String value) {
        if (capabilityUrls == null) {
            capabilityUrls = new HashMap();
        }

        capabilityUrls.put(key, value);
    }

    public String getCapabilityUrl(String key) {
        return (String) capabilityUrls.get(key);
    }

    ///////////////////////////////////////////////////////////////////////
    public void preprocess() {
        // by default does nothing
        //subclasses can override
    }

    public void postprocess() {
        // by default does nothing
        //subclasses can override
    }

    void setContext(HashMap transactionContext) {
        if (transactionContext != null) {
            this.transactionContext = transactionContext;

            capabilityUrls = (HashMap) transactionContext.get("capabilityUrls");

            if (capabilityUrls == null) {
                capabilityUrls = new HashMap();
                transactionContext.put("capabilityUrls", capabilityUrls);
            }
        }
    }

    public HashMap getTransactionContext() {
        return transactionContext;
    }

    public HashMap getResponseHeaderMap() {
        return responseHeaderMap;
    }

    public void setResponseHeaderMap(HashMap responseHeaders) {
        responseHeaderMap = responseHeaders;
    }

    /**
     * Returns the value of the response header with the specified name, or <code>null</code>
     * if the header was not returned.
     *
     * @param headerName The name of the header to be retrieved.
     */
    public String getResponseHeader(String headerName) {
        String responseString = null;
        // If we have no header map, we obviously have no headers. Also, if
        // there is no list for the header name, we don't have the
        // requested header.
        if (headerName != null && headerName.equals("content-type")) {
            headerName = "Content-Type";
        }
        if (responseHeaderMap != null) {
            logger.debug("RESPONSEHEADERMAP ==> " + responseHeaderMap.toString());
//            responseString = (String) responseHeaderMap.get(headerName.toLowerCase());
            logger.debug("ContentType Class is ... " + responseHeaderMap.get(headerName).getClass().getName());
            Object object = responseHeaderMap.get(headerName);
            if (object == null)
                return null;
            if (object instanceof ArrayList) {
                responseString = (String) ((ArrayList) object).get(0);
            } else
                responseString = object.toString();
        } else {
            logger.debug("RESPONSEHEADERMAP ==> " + responseHeaderMap);
        }
        return responseString;
    }

    /**
     * Getter for property compressionFormat.
     *
     * @return Value of property compressionFormat.
     */
    public String getCompressionFormat() {
        return this.compressionFormat;
    }

    /**
     * Setter for property compressionFormat.
     *
     * @param compressionFormat New value of property compressionFormat.
     */
    public void setCompressionFormat(String compressionFormat) {
        this.compressionFormat = compressionFormat;
    }

    static public void log(String logMessage) {
        logger.debug(logMessage);
    }
}


