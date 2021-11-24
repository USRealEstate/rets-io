//
//  RETSBasicResponseParser.java
//  NARRETSClasses
//
//  Created by Bruce Toback on 1/3/05.
//  Copyright (c) 2005 __MyCompanyName__. All rights reserved.
//

package org.realtor.rets.retsapi;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.Vector;

/**
 * Do a basic parse of a RETS response, optionally failing fast upon determination
 * as to whether this is actually a RETS response.
 */

public class RETSBasicResponseParser extends org.xml.sax.helpers.DefaultHandler
{
    protected String fReplyText;
    protected int fReplyCode;
    protected Vector fExceptions;
    protected StringBuffer fElementCharacters;
    protected boolean fReplyValid;
    protected boolean fFirstElementProcessed;   // Set once the first element is processed

    public RETSBasicResponseParser(InputStream responseStream) {
        fReplyValid = false;

        try {
            SAXParserFactory aParserFactory = SAXParserFactory.newInstance();

            aParserFactory.setValidating(false);
            aParserFactory.setNamespaceAware(true);

            SAXParser aParser = aParserFactory.newSAXParser();

            aParser.parse(responseStream, this);
        } catch (SAXParseException saxParseException) {
            addExceptionMessage(saxParseException.getMessage());
        } catch (SAXException saxException) {
            addExceptionMessage(saxException.getMessage());
        } catch (ParserConfigurationException parserConfigurationException) {
            addExceptionMessage(parserConfigurationException.getMessage());
        } catch (java.io.IOException ioException) {
            addExceptionMessage(ioException.getMessage());
        }
    }

    public boolean responseIsValid() {
        return fReplyValid && (fExceptions == null || fExceptions.size() == 0);
    }

    public Vector exceptionMessages() {
        return fExceptions;
    }

    public int replyCode() {
        return fReplyCode;
    }

    public String replyText() {
        return fReplyText;
    }

    protected void addExceptionMessage(String exceptionMessage) {
        if (fExceptions == null)
            fExceptions = new Vector();
        fExceptions.addElement(exceptionMessage);
    }

    // Methods required to extend DefaultHandler

    public void error(SAXParseException e) {
        addExceptionMessage(e.getMessage());
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        int attributeCount = attributes.getLength();
        int attributeIndex;
        String attributeName;

        if (fElementCharacters == null)
            fElementCharacters = new StringBuffer();
        else
            fElementCharacters.setLength(0);

        if (qName.equals("RETS") ||
                qName.equals("RETS-STATUS")) {
            for (attributeIndex = 0; attributeIndex < attributeCount; ++attributeIndex) {
                attributeName = attributes.getLocalName(attributeIndex);

                if (attributeName.equals("ReplyText"))
                    fReplyText = attributes.getValue(attributeIndex);
                else if (attributeName.equals("ReplyCode")) {
                    String replyCode = attributes.getValue(attributeIndex);
                    try {
                        fReplyCode = Integer.parseInt(replyCode);
                        fReplyValid = true;
                    } catch (NumberFormatException e) {
                        fReplyCode = 0;
                        addExceptionMessage("RETS reply code invalid (\"" + replyCode + "\")");
                    }
                }
            }
        } else if (!fFirstElementProcessed) {
            throw new SAXException("Not a RETS reply.");
        }

        fFirstElementProcessed = true;
    }

    public void endElement(String uri, String localName, String qName) {
    }

    public void characters(char[] ch, int start, int length) {
        fElementCharacters.append(ch, start, length);
    }

}
