/**
 * RETSRequestResponse.java
 *
 * @author jbrush
 * @version
 */
package com.ossez.usreio.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;


///////////////////////////////////////////////////////////////////////
public class RETSRequestResponse implements Serializable {
    private final static Logger logger = LoggerFactory.getLogger(RETSRequestResponse.class);
    private HashMap req = null;
    private HashMap resp = null;

    public RETSRequestResponse() {
        req = new HashMap();
        resp = new HashMap();
    }

    ///////////////////////////////////////////////////////////////////////
    public void setRequestVariable(String key, String value) {
        req.put(key, value);
    }

    public String getRequestVariable(String key) {
        return (String) req.get(key);
    }

    public Map getRequestMap() {
        return (Map) req;
    }

    public void addToRequestMap(Map m) {
        req.putAll(m);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setResponseVariable(String key, String value) {
        resp.put(key, value);
    }

    public String getResponseVariable(String key) {
        return (String) resp.get(key);
    }

    public Map getResponseMap() {
        return (Map) resp;
    }

    public void addToResponseMap(Map m) {
        resp.putAll(m);
    }

    ///////////////////////////////////////////////////////////////////////
}
