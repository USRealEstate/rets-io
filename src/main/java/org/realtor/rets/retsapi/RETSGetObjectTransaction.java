package org.realtor.rets.retsapi;

import org.apache.log4j.Category;

import java.io.IOException;
import java.io.InputStream;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.MessagingException;

/**
 * RETSGetObjectTransaction.java
 *
 * @version 1.0
 * @author	jbrush
 */
public class RETSGetObjectTransaction extends RETSTransaction
{
    /**
     * Encapsulates the RETS GetObject transaction, and provides services for
     * interpreting the result. As with all {@link RETSTransaction} classes,
     * your code should create an instance of this class when it wants to
     * perform a GetObject transaction. The transaction requires three
     * parameters: the resource name (see {@link #setResource}), the type
     * of object requested (e.g., <code>"Photo"</code>; see {@link setType}),
     * and the ID of the object -- that is, its associated key such as the
     * MLS number or agent number. You may also request that the server
     * send back only the location of the resource by calling {@link #setLocation}.
     */

    static Category cat = Category.getInstance(RETSGetObjectTransaction.class);
    // collection of body parts resulting from the collision of the server response with the MIME parsing code.
//    protected Collection fBodyParts;
    protected ArrayList parts;

    /**
     * create new RETSGetObjectTransaction and set RequestType
     */
    public RETSGetObjectTransaction() {
        super();
        setRequestType("GetObject");
    }

    /**
     * Sets the response body for the transaction.
     *
     * @param body body of the transaction
     */
    public void setResponse(String body) {
        super.setResponse(body);
        setKeyValuePairs(body);
    }

    /**
     * Sets the resource attribute to the string passed in.
     *
     * @param str resource value
     */
    public void setResource(String str) {
        cat.debug("set Resource=" + str);
        setRequestVariable("Resource", str);
    }

    /**
     * Sets the type attribute to the string passed in.
     *
     * @param type type attribute value
     */
    public void setType(String str) {
        cat.debug("set Type=" + str);
        setRequestVariable("Type", str);
    }

    /**
     * Sets the ID attribute to the string passed in.
     *
     * @param str ID of the object
     */
    public void setID(String str) {
        if ( str != null ) {
            cat.debug("set ID=" + str.trim());
            setRequestVariable("ID", str.trim());
        } else {
            cat.debug("set ID=" + str);
        }
    }

    /**
     * Sets the location attribute to the string passed in.
     *
     * @param str location attribute value
     */
    public void setLocation(String str) {
        cat.debug("set Location=" + str);
        setRequestVariable("Location", str);
    }

    /**
     * Sets the response stream. This triggers various actions depending on the
     * content-type of the response stream:

     * If the content-type is text/plain or text/xml, then assume we have a
     * RETS response and parse it accordingly. Note that we'll not have
     * anything in the RETS response except error information. (We might
     * have no error and nothing else, in which case the only possibility
     * is that we made a request with Location=1 and the server has
     * responded in kind.) We still make this available, in case there *is*
     * something else in the response.

     * A content-type of<code> multipart</code>, with any subtype, is interpreted as a
     * multipart response. This is parsed to create a list of MIME parts.
     * Any other content type is simply made available as a single MIME part.

     * This method is called by {@link RETSConnection} to provide access to
     * the response from a transaction. You don't normally call this method
     * yourself.
     *
     * @param responseStream The response stream to associate with this transaction.
     * @call Rarely.
     * @override Rarely. You can override this method if you want to provide
     * special processing of a GetObject response stream, but
     * it will usually be more convenient to override one                                                                   
     * of the methods that handles particular MIME types.
     */
    public void setResponseStream(InputStream responseStream) {

        String mimeType;
//        String contentType = responseHeaderNamed("content-type");
        String contentType = super.getResponseHeader("Content-Type");
        cat.debug("====[RETSGetObjectTx] --> " + contentType);
        int contentTypeSemicolonIndex =
                contentType == null ? -1 : contentType.indexOf(";");

        // If there was no Content-Type header, we can't do anything here. Punt to the default handler.
        if (contentType == null) {
            cat.debug("====[RETSGetObjectTx] : NO CONTENT TYPE");
            super.setResponseStream(responseStream);
            return;
        }

        // If the content-type string had parameters, trim them to get just the MIME type.
        if (contentTypeSemicolonIndex >= 0)
            mimeType = contentType.substring(0, contentTypeSemicolonIndex).trim();
        else
            mimeType = contentType.trim();

        cat.debug("====[RETSGetObjectTx] : mime-type -> " + mimeType);

        // If the type is text/xml, then this is probably an error response.
        // We need to parse the input stream nondestructively to find out.
        if (mimeType.equals("text/xml")) {
            handleXMLStream(responseStream, mimeType, contentType);
        } else if (mimeType.startsWith("multipart/")) {
            // If it's multipart, take it apart and set up appropriate data structures.
            cat.debug("====[RETSGetObjectTx] : RECIEVED MULTIPART");
            handleMultipartStream(responseStream, mimeType, contentType);
        } else {
            // Otherwise, since we do have a MIME type, assume that the response *is* object value.
            handleStreamWithType(responseStream, mimeType, contentType);
        }
    }

    /**
     * Handle an input stream whose advertised MIME type is text/xml.
     * This may be a RETS error response or something else; we need to figure
     * out exactly what it is. If it is a RETS response, parse it and
     * deal with it. If not, handle as for an unknown response.
     *
     * @param responseStream The response stream containing the XML data.
     * @call Only from subclasses.
     * @ @override Override to provide your own handling of XML data
     * streams.
     */
    protected void handleXMLStream(InputStream responseStream, String mimeType, String contentType) {
        try {
            InputStreamDataSource responseStreamDataSource =
                    new InputStreamDataSource(responseStream, contentType);
            RETSBasicResponseParser basicResponseParser =
                    new RETSBasicResponseParser(responseStreamDataSource.getInputStream());
            if (basicResponseParser.responseIsValid()) {
                setResponseStatus(Integer.toString(basicResponseParser.replyCode()));
                setResponseStatusText(basicResponseParser.replyText());
            } else {
                makeSinglePartFromByteStream(responseStreamDataSource.contentAsByteArray(), contentType);
                setResponseStatus("0"); // The response is valid in this case, since we got a body that we can provide
            }
        } catch (Exception e) {
            // We really need something better for this.
            setResponseStatus("20513");
            setResponseStatusText("RETSAPI: Could not create a MIME body part from XML stream: " + e.getMessage());
        }
    }

    /**
     * Handle an input stream whose advertised MIME type is <code>multipart</code>.
     * This involves breaking up the stream into its constituent parts
     * for callers to retrieve.
     *
     * @param responseStream The stream to parse.
     * @param mimeType       The MIME type and subtype associated with the stream.
     * @param contentType    The Content-Type header, including the MIME type and its parameters, if any.
     * @call Only from subclasses.
     * @override Override to provide your own handling of MIME multipart
     * <p/>
     * data.
     */
    protected void handleMultipartStream(InputStream responseStream, String mimeType, String contentType) {

        InputStreamDataSource responseStreamDataSource = null;
        try {
            responseStreamDataSource = new InputStreamDataSource(responseStream, contentType);
            MimeMultipart multipartResponse = new MimeMultipart(responseStreamDataSource);
//            multipartResponse.writeTo(System.err);

            parts = new ArrayList();
            int partCount = multipartResponse.getCount();
            for (int i = 0; i < partCount; ++i) {
                parts.add(multipartResponse.getBodyPart(i));

            }

            setResponseStatus("0");
        } catch (MessagingException messagingException) {
            if (responseStreamDataSource != null)
                cat.debug(responseStreamDataSource.bufferedDataAsString());
//                System.out.println(responseStreamDataSource.bufferedDataAsString());

            messagingException.printStackTrace();
            setResponseStatus("20513");
            setResponseStatusText("RETSAPI: Could not create a multipart stream from response: " + messagingException.getMessage());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            setResponseStatus("20513");
            setResponseStatusText("RETSAPI: I/O exception while creating multipart stream from response: " + ioException.getMessage());
        } finally {
            // We always want at least an empty body part list.
            if ( parts == null ) parts = new ArrayList();
        }
    }

    /**
     * Helper for making the response into a single body part. This takes an
     * byte array which may have been created during an earlier
     * phase of processing, rather than taking an <code>InputStream</code>.
     *
     * @param inputBytes  A byte array containing the response data.
     * @param contentType The content-type header.
     * @call Rarely.
     * @override Rarely.
     */
    protected void makeSinglePartFromByteStream(byte[] inputBytes, String contentType) {
        // First, we need to gather the response headers into an InternetHeader object
        InternetHeaders originalHeaders = new InternetHeaders();
        Iterator headerIterator = getResponseHeaderMap().keySet().iterator();

        Object headerContent = null;
        String headerName = null;
        while (headerIterator.hasNext()) {
            headerName = (String) headerIterator.next();
//            String headerContent = (String) getResponseHeaderMap().get(headerName);
            headerContent = getResponseHeaderMap().get(headerName);
            if ( headerContent != null )
                originalHeaders.addHeader(headerName, headerContent.toString());
        }
        parts = new ArrayList(1); // We may not have *any*, but we won't have more than 1.
        try {
            parts.add(new MimeBodyPart(originalHeaders, inputBytes));
            setResponseStatus("0");
        } catch (Exception e) {
            e.printStackTrace();
            setResponseStatus("20513");
            setResponseStatusText("RETSAPI: Could not create a MIME body part from response: " + e.getMessage());
        }
    }

    /**
     * Handle an input stream whose advertised MIME type isn't either
     * multipart or XML. This packages up the stream as its own MIME part
     * in order to offer it through the normal multipart interface.
     *
     * @param responseStream The stream to parse.
     * @param mimeType       The MIME type and subtype associated with the stream.
     * @param contentType    The Content-Type header, including the MIME type and its parameters, if any.
     * @call Only from subclasses.
     * @override Override to provide your own handling of data with special
     * <p/>
     * MIME types.
     */
    protected void handleStreamWithType(InputStream responseStream, String mimeType, String contentType) {
        try {
            makeSinglePartFromByteStream(
                    (new InputStreamDataSource(responseStream, contentType)).contentAsByteArray(), contentType);
        } catch (IOException e) {
            e.printStackTrace();
            setResponseStatus("20513");
            setResponseStatusText("RETSAPI: Could not create a data source from response: " + e.getMessage());
        }
    }

    /**
     * Returns the count of retrieved objects.
     */
    public int getObjectCount() {
        if ( parts == null ) return 0;
        return parts.size();
    }

    /**
     * Returns a vector of all objects found. These are stored as MimeBodyPart objects.
     * The returned vector may be empty.
     */
    public Collection allReturnedObjects() {
        return parts;
    }

    /**
     * Returns the object with the given index as a MIME body part. Returns <code>null</code>
     * if no object with the given index exists.
     *
     * @param objectIndex The index of the object to retrieve.
     */
    public MimeBodyPart partAtIndex(int objectIndex) {
        if ( parts == null || objectIndex < 0 || objectIndex >= parts.size() )
            return null;
        return (MimeBodyPart) parts.get(objectIndex);
    }

    public InputStream getPartAsStream(int objectIndex) {
        InputStream inputStream = null;
        MimeBodyPart part = this.partAtIndex(objectIndex);
        try {
            if ( part != null ) {
                Object content = part.getContent();
                inputStream = (InputStream) content;
                cat.debug("--- MimeBodyPart Content--> " + content);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Kablewie!");
        }
        return inputStream;
    }

}
