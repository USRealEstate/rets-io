package com.ossez.usreio.client.retsapi;

import com.ossez.usreio.common.util.AttributeExtracter;
import com.ossez.usreio.common.util.ResourceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.util.HashMap;

/**
 * RETSSearchTransaction
 *
 * @author YuCheng Hu
 */
public class RETSSearchTransaction extends RETSTransaction {
    private final static Logger logger = LoggerFactory.getLogger(RETSSearchTransaction.class);

    //Required Arguments
    protected static final String SEARCHTYPE = "SearchType";
    protected static final String SEARCHCLASS = "Class";
    protected static final String SEARCHQUERY = "Query";
    protected static final String SEARCHQUERYTYPE = "QueryType";

    // Optional Arguments
    protected static final String SEARCHCOUNT = "Count";
    protected static final String SEARCHFORMAT = "Format";
    protected static final String SEARCHLIMIT = "Limit";
    protected static final String SEARCHOFFSET = "Offset";
    protected static final String SEARCHSELECT = "Select";
    protected static final String SEARCHDELIMITER = "DELIMITER";
    protected static final String SEARCHRESTRICTEDINDICATOR = "RestrictedIndicator";
    protected static final String SEARCHSTANDARDNAMES = "StandardNames";
    private String version = null;

    public RETSSearchTransaction() {
        super();
        setRequestType("Search");
        setSearchQueryType("DMQL");
    }

    public void setResponse(String body) {
        super.setResponse(body);

        HashMap hm = this.getAttributeHash(body);
        processXML(hm);
    }

    ///////////////////////////////////////////////////////////////////////

    /*    void processCompact(String body) {
            processCountTag(body);
            processDelimiterTag(body);
            processColumnTag(body);
            processCompactData(body);
            processMaxRowTag(body);
        } */
    void processXML(HashMap hash) {
        if (hash == null) {
            return;
        }

        processCountTag((HashMap) hash.get("COUNT"));
        processXMLData((HashMap) hash.get("BODY"));
        processMaxRowTag((HashMap) hash.get("MAXROWS"));
        processDelimiterTag((HashMap) hash.get("DELIMITER"));
    }

    private HashMap getAttributeHash(String body) {
        AttributeExtracter ae = new AttributeExtracter();
        DefaultHandler h = ae;

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser p = spf.newSAXParser();
            ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());

            p.parse(bais, h, "file:/" + ResourceLocator.locate("dummy.dtd"));
        } catch (Exception e) {
            logger.warn("Hash Error:", e);

            return null;
        }

        return ae.getHash();
    }

    void processCountTag(HashMap hash) {
        if (hash == null) {
            return;
        }

        String records = (String) hash.get("Records");
        if (records == null) {
            records = (String) hash.get("records");
        }
        if (records == null) {
            records = (String) hash.get("RECORDS");
        }
        setCount(records);
    }

    void processDelimiterTag(HashMap hash) {
        if (hash == null) {
            return;
        }

        String delim = (String) hash.get("value");

        if (delim == null) {
            delim = (String) hash.get("VALUE");
        }

        if (delim == null) {
            delim = (String) hash.get("Value");
        }

        setSearchDelimiter(delim);
    }

    void processColumnTag(HashMap hash) {
    }

    void processCompactData(HashMap hash) {
    }

    void processXMLData(HashMap hash) {
    }

    void processMaxRowTag(HashMap hash) {
        if (hash == null) {
            setResponseVariable("MAXROWS", "true");
        }

        //  else
        //    setResponseVariable("MAXROWS", "false");
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchType(String searchType) {
        setRequestVariable(SEARCHTYPE, searchType);
    }

    public String getSearchType() {
        return getRequestVariable(SEARCHTYPE);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchClass(String searchClass) {
        setRequestVariable(SEARCHCLASS, searchClass);
    }

    public String getSearchClass() {
        return getRequestVariable(SEARCHCLASS);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchQuery(String searchQuery) {
        setRequestVariable(SEARCHQUERY, searchQuery);
    }

    public String getSearchQuery() {
        return getRequestVariable(SEARCHQUERY);
    }

    public void setQuery(String searchQuery) {
        setRequestVariable(SEARCHQUERY, searchQuery);
    }

    public String getQuery() {
        return getRequestVariable(SEARCHQUERY);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchQueryType(String searchQueryType) {
        setRequestVariable(SEARCHQUERYTYPE, searchQueryType);
    }

    public String getSearchQueryType() {
        return getRequestVariable(SEARCHQUERYTYPE);
    }

    public void setQueryType(String searchQueryType) {
        setRequestVariable(SEARCHQUERYTYPE, searchQueryType);
    }

    public String getQueryType() {
        return getRequestVariable(SEARCHQUERYTYPE);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchCount(String value) {
        setRequestVariable(SEARCHCOUNT, value);
    }

    public String getSearchCount() {
        return getRequestVariable(SEARCHCOUNT);
    }

    public void setCount(String value) {
        setRequestVariable(SEARCHCOUNT, value);
    }

    public String getCount() {
        return getRequestVariable(SEARCHCOUNT);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchFormat(String value) {
        setRequestVariable(SEARCHFORMAT, value);
    }

    public String getSearchFormat() {
        return getRequestVariable(SEARCHFORMAT);
    }

    public void setFormat(String value) {
        setRequestVariable(SEARCHFORMAT, value);
    }

    public String getFormat() {
        return getRequestVariable(SEARCHFORMAT);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchLimit(String value) {
        setRequestVariable(SEARCHLIMIT, value);
    }

    public String getSearchLimit() {
        return getRequestVariable(SEARCHLIMIT);
    }

    public void setLimit(String value) {
        setRequestVariable(SEARCHLIMIT, value);
    }

    public String getLimit() {
        return getRequestVariable(SEARCHLIMIT);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchOffset(String value) {
        setRequestVariable(SEARCHOFFSET, value);
    }

    public String getSearchOffset() {
        return getRequestVariable(SEARCHOFFSET);
    }

    public void setOffset(String value) {
        setRequestVariable(SEARCHOFFSET, value);
    }

    public String getOffset() {
        return getRequestVariable(SEARCHOFFSET);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchSelect(String value) {
        setRequestVariable(SEARCHSELECT, value);
    }

    public String getSearchSelect() {
        return getRequestVariable(SEARCHSELECT);
    }

    public void setSelect(String value) {
        setRequestVariable(SEARCHSELECT, value);
    }

    public String getSelect() {
        return getRequestVariable(SEARCHSELECT);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchDelimiter(String value) {
        setRequestVariable(SEARCHDELIMITER, value);
    }

    public String getSearchDelimiter() {
        return getRequestVariable(SEARCHDELIMITER);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchRestrictedIndicator(String value) {
        setRequestVariable(SEARCHRESTRICTEDINDICATOR, value);
    }

    public String getSearchRestrictedIndicator() {
        return getRequestVariable(SEARCHRESTRICTEDINDICATOR);
    }

    ///////////////////////////////////////////////////////////////////////
    public void setSearchStandardNames(String value) {
        setRequestVariable(SEARCHSTANDARDNAMES, value);
    }

    public String getSearchStandardNames() {
        return getRequestVariable(SEARCHSTANDARDNAMES);
    }

    public void setStandardNames(String value) {
        setRequestVariable(SEARCHSTANDARDNAMES, value);
    }

    public String getStandardNames() {
        return getRequestVariable(SEARCHSTANDARDNAMES);
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
