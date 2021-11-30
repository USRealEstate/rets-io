/* $Header: /usr/local/cvsroot/rets/commons/src/main/java/org/realtor/rets/util/CompactFormatData.java,v 1.2 2005/05/26 17:43:55 ekovach Exp $
 */
package com.ossez.usreio.common.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A class to represent set of compact-format RETS data.
 */
public class CompactFormatData {
    /**
     * Final value to denote that the <DELIMITER> tag hasn't been read yet.
     */
    private static final char NO_DELIMITER_CHAR = 0;

    /**
     * Storage for the column names.  Set to null to show that the colum names
     * have not been read.
     */
    private ArrayList columnArrayList = null;

    /**
     * Storage for the data.
     */
    private ArrayList dataArrayList = new ArrayList();

    /**
     * The delimiter character.
     */
    private char delimiter = NO_DELIMITER_CHAR;

    /**
     * Return a CompactFormatData object with information from the specified Reader.
     *
     * @param reader A Reader from which to read the compact-format information.
     * @return
     * @throws IOException
     */
    public static CompactFormatData parse(Reader reader) throws IOException {
        return new CompactFormatData(reader);
    }

    /**
     * Return a CompactFormatData object with information from the specified
     * String body.
     *
     * @param body A String from which to read the compact-format information.
     */
    public static CompactFormatData parse(String body) throws IOException {
        return new CompactFormatData(new StringReader(body));
    }

    /**
     * Construct a CompactFormatData object with information from a Reader.
     *
     * @param reader A Reader from which to read the compact-format information.
     */
    private CompactFormatData(Reader reader) throws IOException {
        CharArrayWriter caw = new CharArrayWriter();
        int character = -1;
        do {
            character = reader.read();
            if (character >= 0) {
                caw.write(character);
            }
            String testString = caw.toString().trim();
            // check for end-of-file or end-of-CF-line tags
            if ((character < 0) ||
                    testString.endsWith("</RETS>") ||
                    testString.endsWith("</DELIMITER>") ||
                    testString.endsWith("</COLUMNS>") ||
                    testString.endsWith("</DATA>") ||
                    testString.endsWith("</DATA>") ||
                    (testString.startsWith("<RETS") && testString.endsWith(">")) ||
                    (testString.startsWith("<DELIMITER") && testString.endsWith("/>")) ||
                    (testString.startsWith("<COLUMNS") && testString.endsWith("/>")) ||
                    (testString.startsWith("<DATA") && testString.endsWith("/>"))
            ) {
                processLine(testString);
                caw = new CharArrayWriter();
            }

        }
        while (character >= 0);

        if (delimiter == NO_DELIMITER_CHAR) {
            throw new IOException("<DELIMITER> not found");
        }
    }

    /**
     * Process a line of CompactFormatData text.
     *
     * @param line A line of CompactFormatData text.
     */
    private void processLine(String line) throws IOException {
        if (line.startsWith("<DELIMITER ")) {
            StringTokenizer tokenizer = new StringTokenizer(line, "\"");
            tokenizer.nextToken();
            delimiter = (char) Integer.parseInt(tokenizer.nextToken());
        } else if (line.startsWith("<COLUMNS>")) {
            if (delimiter == NO_DELIMITER_CHAR) {
                throw new IOException("<DELIMITER> not specified before <COLUMNS>");
            }
            columnArrayList = readDataLine(line, delimiter, "</COLUMNS>");
        } else if (line.startsWith("<DATA>")) {
            if (delimiter == NO_DELIMITER_CHAR) {
                throw new IOException("<DELIMITER> not specified before <DATA>");
            }
            if (columnArrayList == null) {
                throw new IOException("<COLUMNS> not specified before <DATA>");
            }
            ArrayList tempArrayList = readDataLine(line, delimiter, "</DATA>");
            dataArrayList.add(tempArrayList);
        }
    }

    /**
     * Read a data/column line using the specified delimiter and end tag.
     *
     * @param line      The String line to read.
     * @param delimiter The delimiter character.
     * @param endTag    The token that signifies the end of data/column reading.
     */
    private ArrayList readDataLine(String line, char delimiter, String endTag) {
        ArrayList returnArrayList = new ArrayList();
        int startIndex = 0;
        boolean isFirstToken = true;
        do {
            // get the end index (the next delimiter or the end of the line)
            int endIndex = line.indexOf(delimiter, startIndex);
            if (endIndex < 0) {
                endIndex = line.length();
            }

            // get the token (space between start and delimiter/end of line)
            String token = line.substring(startIndex, endIndex);

            // unquote tokens wrapped in quotes
            if (token.startsWith("\"")) {
                token = token.substring(1, token.length() - 1);
            }

            // if the end token, break out of the loop
            if (token.equals(endTag)) {
                break;
            }

            // skip the first <COLUMN> or <DATA> token
            if (!isFirstToken) {
                returnArrayList.add(token);
            }
            isFirstToken = false;

            // check for the end of the line or the end tag
            startIndex = endIndex + 1;
            if (endIndex >= line.length()) {
                break;
            }
        }
        while (true);
        return returnArrayList;
    }

    /**
     * Get a String array of the column names.  Returns an empty String array
     * if no column names were read.
     *
     * @return A String array of the column names or an empty String array if
     * no column names were read.
     */
    public String[] getColumns() {
        return (String[]) getColumnsAsList().toArray(new String[columnArrayList.size()]);
    }

    /**
     * Get a List of the column names.  Returns an empty list if no column
     * names were read.
     *
     * @return A List of the column names or an empty list if no column
     * names were read.
     */
    public List getColumnsAsList() {
        List returnList = columnArrayList;
        if (returnList == null) {
            returnList = new ArrayList();
        }
        return returnList;
    }

    /**
     * Get the number of data rows.
     *
     * @return The number of data rows.
     */
    public int getDataRowCount() {
        return dataArrayList.size();
    }

    /**
     * Get a String array of data for a particular row.
     *
     * @param row The row number for which to get the data.
     * @return A String array of data for a particular row.
     */
    public String[] getDataForRow(int row) {
        List rowList = getDataForRowAsList(row);
        return (String[]) rowList.toArray(new String[rowList.size()]);
    }

    /**
     * Get a List of data for a particular row.
     *
     * @param row The row number for which to get the data.
     * @return A List of data for a particular row.
     */
    public List getDataForRowAsList(int row) {
        return (List) dataArrayList.get(row);
    }

    /**
     * Get a sublist of the row data.
     *
     * @param startRow
     * @param endRow
     * @return A List of List objects of the requested row data.
     */
    public List getDataSublist(int startRow, int endRow) {
        return dataArrayList.subList(startRow, endRow);
    }

    /**
     * Get a String array of row data for a specific column name.  Returns
     * an empty array if the column is not found.
     *
     * @param columnName The column name for which to fetch data.
     * @return A String array of the row data or an empty array if the column
     * is not found.
     */
    public String[] getDataForColumn(String columnName) {
        List dataList = getDataForColumnAsList(columnName);
        return (String[]) (dataList.toArray(new String[dataList.size()]));
    }

    /**
     * Get a List of row data for a specific column name.  Returns an
     * empty list if the column is not found.
     *
     * @param columnName The column name for which to fetch data.
     * @return A List of the row data or an empty List if the column
     * is not found.
     */
    public List getDataForColumnAsList(String columnName) {
        ArrayList returnList = new ArrayList();
        if (columnArrayList != null) {
            int columnNumber = columnArrayList.indexOf(columnName);
            if (columnNumber >= 0) {
                Iterator iterator = dataArrayList.iterator();
                while (iterator.hasNext()) {
                    List rowList = (List) iterator.next();
                    if (rowList.size() > columnNumber) {
                        returnList.add(rowList.get(columnNumber));
                    } else {
                        returnList.add("");
                    }
                }
            }
        }
        return returnList;
    }

    /**
     * Get a String representation of this CompactFormatData.
     *
     * @return A String representation of this CompactFormatData.
     */
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("CompactFormatData: Columns ");
        if (columnArrayList == null) {
            stringBuffer.append("(none)");
        } else {
            stringBuffer.append(columnArrayList);
        }
        stringBuffer.append(" Data ");
        stringBuffer.append(dataArrayList);
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        try {
            CompactFormatData cfd = parse(new FileReader("C:\\downloads\\cf.txt"));
            System.err.println(cfd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}