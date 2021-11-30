/* $Header: /usr/local/cvsroot/rets/commons/src/main/java/org/realtor/rets/util/CompactParser.java,v 1.2 2003/12/04 15:27:03 rsegelman Exp $  */
package com.ossez.usreio.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 *  CompactParser.java Created Aug 1, 2003
 *  This is a lightweight parser for RETS compact messages. It scans the compact message and loads
 *  the keys and values into its internal data structure. The data can then be iterated over by calling the
 *  nextColumn() method.
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class CompactParser {
    private String delimiter = "\t";
    private LinkedHashMap map;
    private Iterator iter = null;

    public CompactParser() {
        map = new LinkedHashMap();
    }

    public CompactParser(String rawXML) {
        this();
        parse(rawXML);
    }

    public CompactParser(String rawCompact, String metadataXML) {
        this();
        parse(rawCompact);
        System.out.println("Metadata:\n" + metadataXML);
    }

    /**
     * resets the iterator to start at the first column
     *
     */
    public void reset() {
        ArrayList toSort = new ArrayList();
        toSort.addAll(map.keySet());
        Collections.sort(toSort);
        iter = toSort.iterator();
    }

    /**
     *  Returns a Vector conaining the names of the columns.
     * @return The names of the columns.
     */
    public Vector getColumns() {
        Vector rv = new Vector();
        Set s = map.keySet();

        rv.add(s);

        return rv;
    }

    /**
     *  Tests whether or not there are more columns in the iteration.
     * @return true if there are more columns waiting, false if there aren't.
     */
    public boolean hasMoreColumns() {
        if (iter == null) {
            reset();
        }

        return iter.hasNext();
    }

    /**
     *  Get the next column in the sequence.
     * @return A string whose value is the name of the next column, or null if there aren't any more columns.
     */
    public String nextColumn() {
        String rv = null;

        if (iter == null) {
            reset();
        }

        if (iter.hasNext()) {
            rv = (String) iter.next();
        } else {
            iter = null;
        }

        return rv;
    }

    /**
     *  Returns the data contained in the column name represented by the String key
     * @param key The name of the column whose data you wish to examine.
     * @return The data contained in the column, or null if no data is present.
     */
    public String getData(String key) {
        String rv = null;

        if (map.containsKey(key)) {
            rv = (String) map.get(key);
        }

        return rv;
    }

    private void parse(String xml) {
        String tag = getTag("RETS", xml);
        System.out.println("first tag is " + tag);

        String delimTag = getTag("DELIMITER", xml);
        String delim = getAttribute("value", delimTag);

        if (delim != null) {
            delimiter = String.valueOf((char) Integer.parseInt(delim));
        }

        map(getTagBody("COLUMNS", xml), getTagBody("DATA", xml));
    }

    private String getTag(String tagName, String xml) {
        int tagStart = xml.indexOf("<" + tagName);
        int tagEnd = xml.indexOf(">", tagStart) + 1;

        return xml.substring(tagStart, tagEnd);
    }

    private String getTagBody(String tagName, String xml) {
        int tagStart = xml.indexOf(">", xml.indexOf(tagName)) + 1;
        int tagEnd = xml.indexOf("</" + tagName, tagStart);
        System.out.println("Tag Start: " + tagStart + " tag end: " + tagEnd);

        return xml.substring(tagStart, tagEnd);
    }

    private String getAttribute(String attributeName, String tag) {
        String delims = "<>=/";
        StringTokenizer st = new StringTokenizer(tag.substring(tag.indexOf(
                        attributeName)), delims, false);

        while (st.hasMoreTokens()) {
            String next = st.nextToken();

            if (next.equals(attributeName)) {
                // the next value is the quoted value
                String value = st.nextToken();

                return value.substring(1, value.trim().length() - 1);
            }
        }

        return null;
    }

    private void map(String columns, String data) {
        String[] colArr = columns.split(delimiter);
        String[] dataArr = data.split(delimiter);

        for (int i = 0; i < colArr.length; i++) {
            if ((colArr[i] != null) && (colArr[i].length() > 0)) {
                if (dataArr.length > i) {
                    map.put(colArr[i], DigestUtil.removeQuotes(dataArr[i]));
                } else {
                    map.put(colArr[i], "");
                }
            }
        }

        System.out.println(map);
    }

    public Map getMapping() {
        return map;
    }

    public static CompactParser getTestInstance() {
        String xml =
            "<RETS ReplyCode=\"0\" ReplyText=\"Operation successful\" ><DELIMITER value=\"9\"/>" +
            "<COLUMNS>	ListOfficeOfficeID	ListAgentPager	ListOfficeEmail	ListAgentPostalCode	Style	DaysOnMarket	LotSizeArea	Roof	City	CoveredParking	ListAgentFax	ListingStatus	ListOfficePostalCode	ListAgentOfficePhone	StreetNumber	PictureData	TaxID	BathsHalf	ListOfficeOfficePhone	ListOfficeStreetAdditionalInfo	LivingRoomDim	PostalCode	LivingRoom	State	ListType	Utilities	ListingArea	Beds	Zoning	ShowingInstructions	ListAgentEmail	Heating	Cooling	Exterior	AssociationFee	ListAgentCellPhone	FamilyRoom	ListAgentHomePhone	Longitude	ListPrice	ListingID	Baths	StreetDirPrefix	PublicRemarks	ListAgentAgentID	ListAgentLastName	ExpirationData	FamilyRoomDim	ClosePrice	ListAgentFirstName	County	Garage	LivingArea	SaleAgentAgentID	ListOfficeFax	ListOfficeCity	LotSizeDim	BoxNumber	StatusChangeDate	StreetName	Basement	ListAgentStreetAdditionalInfo	ListAgentNRDSMemberID	Fireplaces	ListAgentCity	ListDate	Stories	ListOfficeStreetName	ListOfficeWWW	Sundivision	OriginalListPrice	SaleOfficeOfficeID	Directions	ListOfficeListingServiceName	Remarks	DiningRoomDim	TaxLegalDescription	ListAgentWWW	ListAgentStreetName	SchoolDistrict	ListOfficeState	ListAgentState	Latitude	MapCoordinate	CloseDate	DiningRoom	BathsFull	TotalRooms	</COLUMNS>" +
            "<DATA>	2309			44124	2	860	G	B	CLEVELAND	3		act	44124	(216) 226-4352	3340		1606093	0	(440) 449-2300		12X14	44102	K	OH	ers	0.00	105.00	4		 Call ListAgt	nverikakis@core.com	\"2,3,B,C\"		\"A,C\"	0.00				-81.72779900	74500.00	1004685	2	W		362265	Verikakis	0000-00-00		0.00	Nicholas	CUY	\"1,B,E\"	1542.00		(440) 449-2574	Mayfield Heights	35X125		0000-00-00	61 ST				0	Mayfield Heights	2002-08-08	2	6030 Mayfield Road #1			74900.00		BETWEEN STORER and CLARK	\"Jennie Chiccola Realty, Inc.\"	\"JUST RENOVATED*NEW DRIVE,ROOF,GAR.VINYL+NEW ROOF,FURNACE,HWT KITCHEN,2BATHS,CARPETS,ELECTR,ALL COPPER PLMB and DRAINS PVC, MOST DRYWALL NEW,PORCHES,FENCE,BASMT CONC.FLOOR,CEDAR CLOSET UP*OFF KITCHEN 1ST FLOOR LAUNDRY and REAR DECK*ROOM SIZES ARE APPROXIMATE*\"	00X00			6030 Mayfield Road #1	1809	OH	OH	41.46359100	D1	0000-00-00	K	2	7	</DATA>";
        String shortTest = "<RETS ReplyCode=\"0\" ReplyText=\"Successful\"><DATA> this is a test</DATA></RETS>";
        CompactParser cp = new CompactParser(xml);

        return cp;
    }

    public static void main(String[] args) {
        CompactParser cp = CompactParser.getTestInstance();

        while (cp.hasMoreColumns()) {
            String col = cp.nextColumn();
            System.out.println("Mapping: " + col + " : " + cp.getData(col));
        }

        System.out.println("Hey!");
        cp.reset();
        System.out.println(cp.nextColumn());
    }
}
