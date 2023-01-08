package com.ossez.usreio.common.util;

import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * SessionUtils for RETS server session
 *
 * @author YuCheng Hu
 */
public final class ZIPUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZIPUtils.class);


    /**
     *
     * @param input
     * @param output
     * @throws IOException
     */
    public static void compressGZIP(File input, File output) throws IOException {
        try (GzipCompressorOutputStream out = new GzipCompressorOutputStream(new FileOutputStream(output))) {
            IOUtils.copy(new FileInputStream(input), out);
        } catch (Exception IOException) {

        }
    }

    /**
     *
     * @param sourceFile
     * @return
     * @throws IOException
     */
    public static File compressGZIP(File sourceFile) throws IOException {
        // Check File
        if (!sourceFile.exists()) {
            return null;
        }

        // Get gzipped
        File gzipFile = new File(sourceFile.getParentFile(), GzipUtils.getCompressedFilename(sourceFile.getName()));
        try (GzipCompressorOutputStream out = new GzipCompressorOutputStream(new FileOutputStream(gzipFile))) {
            IOUtils.copy(new FileInputStream(gzipFile), out);
        }
        return gzipFile;
    }
}
