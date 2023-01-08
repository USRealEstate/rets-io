/*
 * RETSCompactHandler.java
 *
 * Created on October 2, 2002, 8:45 AM
 */
package com.ossez.usreio.common.util;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.*;

import java.util.StringTokenizer;

import javax.xml.parsers.*;


//import org.apache.xerces.parsers.SAXParser;
public class RETSCompactHandler extends DefaultHandler {
    private OutputStream os = System.out;
    private String delim = "  ";
    private StringBuffer textBuffer;
    private String currentElement = null;

    public void setOutputStream(OutputStream p_os) {
        this.os = p_os;
    }

    private void nl() throws SAXException {
        String lineEnd = "\r\n";

        try {
            os.write(lineEnd.getBytes());
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    private void emit(String s) throws SAXException {
        try {
            os.write(s.getBytes());
            os.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    public void startDocument() throws SAXException {
        emit("<?xml version='1.0' encoding='UTF-8'?>");
        nl();
    }

    public void endDocument() throws SAXException {
        try {
            nl();
            os.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    public void startElement(String namespaceURI, String sName, // simple name
        String qName, // qualified name
        Attributes attrs) throws SAXException {
        textBuffer = null;

        String eName = qName; // element name
        currentElement = qName;

        if (qName.equalsIgnoreCase("DELIMITER")) {
            // delimiter is a 2 digit HEX value
            String delimOct = attrs.getValue("", "value");
            delim = "" + (char) Integer.parseInt(delimOct, 16);
        }

        if ("".equals(eName)) {
            eName = qName; // not namespaceAware
        }

        emit("<" + eName);

        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name

                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }

                emit(" ");
                emit(aName + "=\"" + attrs.getValue(i) + "\"");
            }
        }

        emit(">");
    }

    public void endElement(String namespaceURI, String sName, // simple name
        String qName // qualified name
    ) throws SAXException {
        if (textBuffer != null) {
            parseText(textBuffer.toString());
        }

        textBuffer = null;

        String eName = qName; // element name

        if ("".equals(eName)) {
            eName = qName; // not namespaceAware
        }

        emit("</" + eName + ">");
        nl();
        currentElement = null;
    }

    public void characters(char[] buf, int offset, int len)
        throws SAXException {
        String s = new String(buf, offset, len);

        if (textBuffer == null) {
            textBuffer = new StringBuffer(s);
        } else {
            textBuffer.append(s);
        }
    }

    public void parseText(String text) throws SAXException {
        if (currentElement == null) {
            emit(text);

            return;
        }

        String eName = "d";

        if (currentElement.equalsIgnoreCase("COLUMNS")) {
            eName = "c";
        }

        String start = "<" + eName + ">";
        String end = "</" + eName + ">";

        if (currentElement.equalsIgnoreCase("COLUMNS") ||
                currentElement.equalsIgnoreCase("DATA")) {
            StringTokenizer st = new StringTokenizer(text, delim, true);
            boolean firstToken = true;
            boolean lastTokenDelim = false;

            while (st.hasMoreTokens()) {
                String token = st.nextToken();

                if (token.equalsIgnoreCase(delim)) {
                    if (lastTokenDelim) {
                        emit(start + end);
                    }

                    lastTokenDelim = true;
                } else {
                    emit(start + token + end);
                    lastTokenDelim = false;
                }
            }
        } else {
            emit(text);
        }
    }

    public static void main(String[] args) {
        DefaultHandler h = new RETSCompactHandler();

        //SAXParser p = new SAXParser();
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser p = spf.newSAXParser();

            FileInputStream fis = new FileInputStream("c:/tmp/xx.xml");
            InputSource is = new InputSource(fis);
            p.parse(is, h);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
