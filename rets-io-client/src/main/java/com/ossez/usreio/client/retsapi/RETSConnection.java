/*
 * RETSConnection.java
 *
 * Created on November 16, 2001, 1:33 PM
 */
package com.ossez.usreio.client.retsapi;

//import com.aftexsw.util.bzip.CBZip2InputStream;

import com.ossez.usreio.common.rets.RetsConfigurator;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.GZIPInputStream;


/**
 * Provides a connection to a RETSServer.
 *
 * @author tweber
 * @version 1.0
 */
public class RETSConnection extends java.lang.Object {
    private final static Logger logger = LoggerFactory.getLogger(RETSConnection.class);

    static {
        RetsConfigurator.configure();
    }

    //Key value pairs for request header.
    private HashMap headerHash = new HashMap();
    private HashMap responseHeaderMap = new HashMap();
    private String serverUrl = null;
    private String errMsg = null;
    private boolean isRetryingAuthorization = false;
    private boolean gzipCompressed = false;
    private boolean bzipCompressed = false;
    private boolean STREAMRESPONSE = false;
    private long lastTransactionTime = 0;
    private String transactionLogDirectory = null;
    private String imageAccept = "image/gif"; // default
    private PrintWriter log = null;
    private HttpClient client = new HttpClient();

    HashMap transactionContext = new HashMap(); // holds data across transactions
    private int connTimeoutSeconds = 60; // 60 seconds default

    /**
     * Creates new RETSConnection and changes default connection timeout
     * and sets the ServerURL.
     */
    public RETSConnection(String url, int connTimeoutSeconds) {
        this(url);
        this.connTimeoutSeconds = connTimeoutSeconds;
    }

    /**
     * Creates new RETSConnection and changes default connection timeout
     * and sets the ServerURL.
     */
    public RETSConnection(int connTimeoutSeconds) {
        this();
        this.connTimeoutSeconds = connTimeoutSeconds;
    }

    /**
     * Creates new RETSConnection and sets the ServerURL.
     */
    public RETSConnection(String url) {
        this();
        serverUrl = url;
    }

    /**
     * Create a new RETSConnection and setup some required Header fields.
     */
    public RETSConnection() {
        setRequestHeaderField("User-Agent", "Mozilla/4.0");
        setRequestHeaderField("RETS-Version", "RETS/1.0");
    }

    /**
     * Executes a transaction
     *
     * @param transaction transaction to execute
     */
    public void execute(RETSTransaction transaction) {
        execute(transaction, false);
    }

    /**
     * Executes a transaction
     *
     * @param transaction transaction to execute
     */
    public void executeStreamResponse(RETSTransaction transaction) {
        execute(transaction, true);
    }

    /**
     * Executes a transaction
     *
     * @param transaction transaction to execute
     * @param asStream
     */
    public void execute(RETSTransaction transaction, boolean asStream) {
        java.util.Date dt1 = new Date();
        STREAMRESPONSE = asStream;

        if (transaction instanceof RETSGetObjectTransaction) {
            setRequestHeaderField("Accept", getImageAccept());
        } else {
            setRequestHeaderField("Accept", "*/*");
        }

        if ((transactionLogDirectory != null) && (transactionLogDirectory.length() > 1)) {
            String transType = transaction.getClass().getName();
            int nameIdx = transType.lastIndexOf(".") + 1;
            String name = transType.substring(nameIdx);
            Date dt = new Date();
            String outFile = transactionLogDirectory + "/" + name + dt.getTime() + ".txt";

            try {
                log = new PrintWriter(new FileWriter(outFile));
                log.println("<!-- RETS REQUEST -->");
            } catch (Exception e) {
                logger.error("could create output file :" + outFile);
            }
        }

        String compressFmt = transaction.getCompressionFormat();

        if (compressFmt != null) {
            if (compressFmt.equalsIgnoreCase("gzip")) {
                setRequestHeaderField("Accept-Encoding", "application/gzip,gzip");
            } else if (compressFmt.equalsIgnoreCase("bzip")) {
                setRequestHeaderField("Accept-Encoding", "application/bzip,bzip");
            } else if (compressFmt.equalsIgnoreCase("none")) {
                removeRequestHeaderField("Accept-Encoding");
            }
        }

        transaction.setContext(transactionContext);

        transaction.preprocess();

        processRETSTransaction(transaction);

        transaction.postprocess();

        Date dt2 = new Date();
        lastTransactionTime = dt2.getTime() - dt1.getTime();

        if (log != null) {
            try {
                log.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            log = null;
        }

        return;
    }

    public long getLastTransactionTime() {
        return lastTransactionTime;
    }

    public void setTransactionLogDirectory(String tLogDir) {
        this.transactionLogDirectory = tLogDir;
    }

    public String getTransactionLogDirectory() {
        return this.transactionLogDirectory;
    }

    private void writeToTransactionLog(String msg) {
        if (log != null) {
            try {
                this.log.println(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        logger.debug(msg);
    }

    private void writeMapToTransactionLog(Map map) {
        if (map == null) {
            return;
        }

        Iterator itr = map.keySet().iterator();

        while (itr.hasNext()) {
            String key = (String) itr.next();
            String value = "";
            Object obj = map.get(key);

            if (obj instanceof String) {
                value = (String) obj;
            } else {
                value = "{ ";

                Collection c = (Collection) obj;
                Iterator i2 = c.iterator();

                if (i2.hasNext()) {
                    value = (String) i2.next();

                    while (i2.hasNext()) {
                        value = value + ", " + (String) i2.next();
                    }
                }

                value = value + " }";
            }

            writeToTransactionLog(key + "=" + value);
        }
    }

    /**
     * Returns the server's URL, this url as a base for all transactions
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * Sets the url for the connection.
     *
     * @param url Server's address ex: http://www.realtor.org/RETSServer
     */
    public void setServerUrl(String url) {
        serverUrl = url;
    }

    /**
     * Key value pairs in the client request header
     *
     * @param key   field name in the request header
     * @param value value associated with the key
     */
    public void setRequestHeaderField(String key, String value) {
        headerHash.put(key, value);
    }

    public void setUserAgent(String userAgent) {
        headerHash.put("User-Agent", userAgent);
    }

    public void setRetsVersion(String retsVersion) {
        setRequestHeaderField("RETS-Version", retsVersion);
    }

    /**
     * Removes a key/value pair from the request header.
     *
     * @param key field to remove from the request header.
     */
    public void removeRequestHeaderField(String key) {
        headerHash.remove(key);
    }

    public HashMap getResponseHeaderMap() {
        return responseHeaderMap;
    }

    /**
     * gets the url content and returns an inputstream
     *
     * @param strURL
     * @param requestMethod
     * @param requestMap
     */
    public InputStream getURLContent(String strURL, String requestMethod, Map requestMap) {
        InputStream is = null;
        gzipCompressed = false;
        bzipCompressed = false;

        boolean needToAuth = false;

        HttpMethod method = null;

        logger.debug("getURLContent: URL=" + strURL);

        try {
            if (requestMethod.equalsIgnoreCase("GET")) {
                method = new GetMethod(strURL);
            } else {
                method = new PostMethod(strURL);
            }

            client.getState().setCredentials(null, null, new UsernamePasswordCredentials(getUsername(), getPassword()));
            client.getState().setCookiePolicy(CookiePolicy.COMPATIBILITY);
            client.setConnectionTimeout(connTimeoutSeconds * 1000);

            method.setDoAuthentication(true);
//            method.setFollowRedirects(true);

            addHeaders(method, headerHash);
            writeMapToTransactionLog(headerHash);

            // send the request parameters
            if (requestMap != null) {
                NameValuePair[] pairs = mapToNameValuePairs(requestMap);

                if (requestMethod.equalsIgnoreCase("POST")) {
                    // requestMethod is a post, so we can safely cast.
                    PostMethod post = (PostMethod) method;
                    post.setRequestBody(pairs);
                } else {
                    GetMethod get = (GetMethod) method;
                    get.setQueryString(pairs);
                }
            }

            this.writeToTransactionLog("<!-- Response from server -->");

            int responseCode = client.executeMethod(method);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ByteArrayInputStream bais = new ByteArrayInputStream(method.getResponseBody());
            copyResponseHeaders(method);
            method.releaseConnection(); // from bruce
            return bais;
        } catch (IOException io) {
            io.printStackTrace();
            errMsg = "RETSAPI: I/O exception while processing transaction: " + io.getMessage();
            return null;
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }

    /**
     * Changes a map into an array of name value pairs
     *
     * @param requestMap The map to change.
     * @return An array of Name value pairs, representing keys and values from the map.
     */
    private NameValuePair[] mapToNameValuePairs(Map requestMap) {
        NameValuePair[] pairs = new NameValuePair[requestMap.size()];
        Iterator iter = requestMap.keySet().iterator();
        int i = 0;

        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = (String) requestMap.get(key);
            NameValuePair nvp = new NameValuePair(key, value);
            pairs[i] = nvp;
            i++;
        }

        return pairs;
    }

    /**
     * Adds response headers to Http method
     *
     * @param responseHeaderMap
     * @param method
     */
    private void copyResponseHeaders(HttpMethod method) {
        responseHeaderMap.clear();

        Header[] headers = method.getResponseHeaders();

        for (int i = 0; i < headers.length; i++) {
            Header current = headers[i];
            List list = (List) responseHeaderMap.get(current.getName());

            if (list == null) {
                list = new ArrayList();
            }

            list.add(current.getValue());
            responseHeaderMap.put(current.getName(), list);
        }
    }

    private void addHeaders(HttpMethod method, HashMap headers) {
        Iterator keys = headers.keySet().iterator();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = headers.get(key);

            if (value instanceof String && isValidString((String) value)) {
                method.addRequestHeader(key, (String) value);
            } else if (value instanceof ArrayList) {
                ArrayList list = (ArrayList) value;
                StringBuffer valueList = new StringBuffer();

                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) {
                        valueList.append(";");
                    }

                    valueList.append(list.get(i));
                }

                method.addRequestHeader(key, valueList.toString());
            }
        }
    }

    /**
     * Processes a transaction, sends rets request and gets
     * the response stream from the server.  Uncompresses the
     * response stream if compression was used in the reply
     *
     * @param transaction rets transaction to process
     */
    private void processRETSTransaction(RETSTransaction transaction) {
        try {
            serverUrl = transaction.getUrl();

            logger.debug(transaction.getRequestType() + " URL : {" + serverUrl + "}");

            if (serverUrl == null) {
                logger.error(transaction.getRequestType() + " URL is null");
                transaction.setResponseStatus("20036");
                transaction.setResponseStatusText(transaction.getRequestType() + " URL is missing. Successful login is required.");
                return; // throw exception here
            }

            String method = "POST";

            // Action transaction requires a GET according to RETS spec
            if (transaction.getRequestType().equalsIgnoreCase("Action")) {
                method = "GET";
            }
            logger.debug("method: " + method);

            InputStream is = getURLContent(serverUrl, method, transaction.getRequestMap());

            if (is == null) {
                transaction.setResponseStatus("20513"); // Miscellaneous error
                transaction.setResponseStatusText(errMsg);
                transaction.setResponse(errMsg);
                errMsg = null;

                return;
            } else {
                Iterator itr = responseHeaderMap.keySet().iterator();
                Object compressionFmt = responseHeaderMap.get("Content-Encoding");

                if (compressionFmt != null) {
                    logger.debug("Header class : " + compressionFmt.getClass().getName());

                    if (compressionFmt.toString().equalsIgnoreCase("[gzip]")) {
                        gzipCompressed = true;
                    } else if (compressionFmt.toString().equalsIgnoreCase("[bzip]")) {
                        bzipCompressed = true;
                    }
                }

                if (gzipCompressed) {
                    is = new GZIPInputStream(is);
                } else if (bzipCompressed) {
//                    is = new CBZip2InputStream(is);
                }
            }
            this.writeToTransactionLog("<!-- Obtained and Identified Response Stream -->");

            transaction.setResponseHeaderMap(this.responseHeaderMap);

            if ((transaction instanceof RETSGetObjectTransaction && (!transaction.getResponseHeader("Content-Type").startsWith("text/xml"))) || STREAMRESPONSE) {
                transaction.setResponseStream(is);
            } else {
                String contents = null;
                contents = streamToString(is);
                writeToTransactionLog(contents);

                /*catch( IOException e) {
                    errMsg = "Error reading response stream: " + contents;
                    cat.error(errMsg, e);
                    transaction.setResponseStatus("20513"); // Miscellaneous error
                    transaction.setResponseStatusText(errMsg);
                    errMsg = null;
                }*/
                if (contents.length() == 0) {
                    transaction.setResponseStatus("20513"); // Miscellaneous error
                    transaction.setResponseStatusText("Empty Body");
                }

                transaction.setResponse(contents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getUsername() {
        String username = null; //(String)requestMap.get("username");

        if (username == null) {
            username = (String) transactionContext.get("username");
        }

        return username;
    }

    String getPassword() {
        String password = null; //(String)requestMap.get("password");

        if (password == null) {
            password = (String) transactionContext.get("password");
        }

        return password;
    }

    /**
     * Removes the quotes on a string.
     *
     * @param quotedString string that might contain quotes
     */
    private static String removeQuotes(String quotedString) {
        if ((quotedString != null) && (quotedString.length() > 2)) {
            return quotedString.substring(1, quotedString.length() - 1);
        } else {
            return ""; // empty string
        }
    }

    /**
     * Checks to make sure the string passed in is a valid string parameter (not null and not zero length).
     *
     * @param value string to be validated
     */
    private boolean isValidString(String value) {
        return ((value != null) && (value.length() > 0));
    }

    private String streamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuffer sb = new StringBuffer();
            int numread = 0;
            byte[] buffer = new byte[1024 * 8]; //initialize an 8k buffer

            while ((numread = is.read(buffer)) >= 0) {
                String s = new String(buffer, 0, numread);
                sb.append(s);
            }

            return sb.toString();
        }

        return null;
    }

    /**
     * Main method for testing only!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        BasicConfigurator.configure();

        RETSConnection rc = new RETSConnection();
        RETSLoginTransaction trans = new RETSLoginTransaction();

        try {
            Properties props = new Properties();
            props.load(new FileInputStream("/tmp/client.properties"));

            // Add the optional request parameters if they exist, are non-null and non-zero-length
            // rc.setRequestHeaderField("Authorization", (String)props.get("login.AUTHORIZATION"));
            rc.setServerUrl((String) props.getProperty("SERVER_URL"));
            trans.setUrl((String) props.getProperty("SERVER_URL"));
            trans.setUsername((String) props.getProperty("USERNAME"));
            trans.setPassword((String) props.getProperty("PASSWORD"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        rc.execute(trans);
    }

    /**
     * Build the queryString from the request map
     *
     * @param requestMap the list of request parameters
     */
    private String buildQueryString(Map requestMap) {
        /*if (((String)(requestMap.get("requestType"))).equalsIgnoreCase("Search")) {
            return "SearchType=Property&Class=RESI&Query=(Listing_Price%3D100000%2B)&QueryType=DMQL";
        }*/
        StringBuffer sb = new StringBuffer();
        Iterator it = requestMap.keySet().iterator();

        // build query string
        while (it.hasNext()) {
            String key = (String) it.next();

            if (key.equals("requestType")) {
                //commenting out requestType because it is not a standard req parameter and may break RETS servers
                continue;
            }

            String reqStr = key + "=" + URLEncoder.encode((String) requestMap.get(key));
            logger.debug(reqStr);
            sb.append(reqStr);

            if (it.hasNext()) {
                sb.append("&");
            }
        }

        return sb.toString();
    }

    public String getImageAccept() {
        return imageAccept;
    }

    public void setImageAccept(String imageAccept) {
        this.imageAccept = imageAccept;
    }
}
