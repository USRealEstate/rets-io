package org.realtor.rets.retsapi;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * A class to provide a {@link javax.activation.DataSource} interface to
 * an input stream of unknown characteristics. The <code>DataSource</code>
 * interface requires that its implementor be able to repeatedly restart
 * the read from the beginning. This isn't guaranteed by InputStream, so
 * we encapsulate the InputStream with an object that will buffer the
 * data coming from it. (We can't use <code>mark</code>/<code>reset</code>
 * because the eventual data source consumer might use those methods,
 * which would override use here.
 */
public class InputStreamDataSource implements DataSource
{
    private byte fStreamBytes[];
    private String fContentType;

    public InputStreamDataSource(InputStream baseStream, String contentType) throws IOException {
        fContentType = contentType;

        // Read the content of the input stream into byte array blocks. Read
        // to the end of file and save all the blocks. These will be consolidated
        // after all are read. This uses twice as much storage as simply designing
        // a new input stream, but I don't want to write that class right now,
        // especially since I'm not completely clear on the blocking semantics.
        // ByteArrayInputStream already knows them, so I'll just use that.

        Vector byteArrays = new Vector();
        int totalBytesRead = 0;
        byte temporaryByteArray[];
        int readCount;
        int quantum = 4096;
        int bytesInCurrentBlock;

        do {
            bytesInCurrentBlock = 0;
            temporaryByteArray = new byte[quantum];
            do {
                readCount =
                        baseStream.read(temporaryByteArray, bytesInCurrentBlock, quantum - bytesInCurrentBlock);
                if (readCount > 0) bytesInCurrentBlock += readCount;
            } while (readCount >= 0 && bytesInCurrentBlock < quantum);

            if (bytesInCurrentBlock > 0)
                byteArrays.add(temporaryByteArray);

            totalBytesRead += bytesInCurrentBlock;
        } while (readCount >= 0);

        // Copy all the blocks into one single mondo block.
        fStreamBytes = new byte[totalBytesRead];

        int numberOfBlocks = byteArrays.size();
        byte theBlock[];
        for (int blockIndex = 0; blockIndex < numberOfBlocks - 1; ++blockIndex) {
            theBlock = (byte[]) byteArrays.get(blockIndex);
            System.arraycopy(theBlock, 0, fStreamBytes, blockIndex * quantum, quantum);
        }

        theBlock = (byte[]) byteArrays.get(numberOfBlocks - 1);
        System.arraycopy(theBlock, 0, fStreamBytes, quantum * (numberOfBlocks - 1), bytesInCurrentBlock);
        
    }

    /**
     * Returns the Content-Type header value for the encapsulated content.
     */
    public String getContentType() {
        return fContentType;
    }

    /**
     * Returns an input stream that may be used to access the content of this
     * <code>DataSource</code> A new input stream, set at the beginning of the
     * stream, is returned each time you call this method.
     *
     * @return An {@link InputStream} that will furnish the
     *         associated data.
     */
    public InputStream getInputStream() {
        return new ByteArrayInputStream(fStreamBytes);
    }

    /**
     * Returns the name of this data source. This class does not provide named data
     * sources; the string "Untitled" is returned.
     *
     * @return The string "Untitled".
     */
    public String getName() {
        return "Untitled";
    }

    /**
     * Conformance to <code>javax.activation.DataSource</code> Throws an
     * {@link IOException} since this DataSource is read-only.
     */
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("InputStreamDataSource is read-only.");
    }

    /**
     * Return the content of the input stream as a full byte array.
     */
    public byte[] contentAsByteArray() {
        return fStreamBytes;
    }

    /**
     * Returns the loaded data as a string. This is primarily for diagnostic
     * purposes, as there are other ways of turning an InputStream into a String.
     *
     * @return A <code>String</code> containing the input data.
     */
    public String bufferedDataAsString() {
        return new String(fStreamBytes);
    }
}

