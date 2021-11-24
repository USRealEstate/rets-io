package org.realtor.rets.retsapi;

import org.apache.log4j.Category;

import java.util.Iterator;
import java.util.Map;
import org.apache.xpath.XPathAPI;
import org.realtor.rets.util.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.Collections;
import java.util.Vector;
import javax.xml.transform.TransformerException;


/**
 *        RETSUpdateTransaction.java
 *
 *        @author        pobrien
 *        @version 1.0
 */
public class RETSUpdateTransaction extends RETSTransaction {
    static Category cat = Category.getInstance(RETSUpdateTransaction.class);

    /**
     *
     */
    public RETSUpdateTransaction() {
        super();
        setRequestType("Update");
        setDelimiter("09");//default is ascii ht
    }

    /**
     *  Sets the response body for the transaction.
     *
     *  @param body body of the transaction
     */
    public void setResponse(String body) {
        super.setResponse(body);
        System.out.println("Setting response as " + body);
        setKeyValuePairs(body);
    }

    /**
     *  Sets the type attribute to the string passed in.
     *
     *  @param type type attribute value
     */
    public void setType(String str) {
        cat.debug("set Type=" + str);
        setRequestVariable("Type", str);
    }

    /**
     *  Sets the ID attribute to the string passed in.
     *
     *  @param str ID of the object
     */
    public void setValidate(String str) {
        cat.debug("set Validate=" + str);
        setRequestVariable("Validate", str);
    }

    /**
     *  Sets the location attribute to the string passed in.
     *
     *  @param str location attribute value
     */
    public void setDelimiter(String str) {
        cat.debug("set Delimiter=" + str);
        setRequestVariable("Delimiter", str);
    }

    public String getDelimiter() {
	    return getRequestVariable("Delimiter");
    }

    public void setRecord(String str) {
        cat.debug("set Record=" + str);
        setRequestVariable("Record", str);
    }

    public void setWarningResponse(String str) {
	        cat.debug("set WarningResponse=" + str);
	        setRequestVariable("WarningResponse", str);
    }

    public void setNewValues(Map m) {
        // convert to a string and feed to setRecord()....
        StringBuffer record = new StringBuffer();
        Iterator iter = m.keySet().iterator();
		// delimiter is a 2 digit HEX value
        char delim = (char) Integer.parseInt(getDelimiter().trim(), 16);

        while (iter.hasNext()) {
            String name = (String) iter.next();
            Object val = m.get(name);
            String value = "";

            if (val instanceof String) {
                value = (String) val;
            } else {
                String[] arr = (String[]) val;
                value = arr[0];
            }

            record.append(name);
            record.append("=");
            record.append(value);

            if (iter.hasNext()) {

                record.append(delim);
            }
        }

        setRecord(record.toString());
    }


    public void setWarningResponseValues(Map m) {
	        // convert to a string and feed to setWarningResponse()....
	        StringBuffer warning = new StringBuffer("(");
	        Iterator iter = m.keySet().iterator();
			// delimiter is a 2 digit HEX value
	        char delim = (char) Integer.parseInt(getDelimiter().trim(), 16);

	        while (iter.hasNext()) {
	            String name = (String) iter.next();
	            Object val = m.get(name);
	            String value = "";

	            if (val instanceof String) {
	                value = (String) val;
	            } else {
	                String[] arr = (String[]) val;
	                value = arr[0];
	            }

	            warning.append(name);
	            warning.append("=");
	            warning.append(value);

	            if (iter.hasNext()) {

	                warning.append(delim);
	            }
	        }

	        warning.append(")");
	        setWarningResponse(warning.toString());
    }

    public void setUID(String id) {
        System.out.println("UID is " + id);
        setRequestVariable("OriginalUid", id);
    }





}
