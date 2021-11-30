// Base64FormatException.java
// $Id: Base64FormatException.java,v 1.2 2003/12/04 15:27:03 rsegelman Exp $
// (c) COPYRIGHT MIT and INRIA, 1996.
// Please first read the full copyright statement in file COPYRIGHT.html
package com.ossez.usreio.common.util;


/**
 * Exception for invalid BASE64 streams.
 */
public class Base64FormatException extends Exception {
    /**
     * Create that kind of exception
     * @param msg The associated error message
     */
    public Base64FormatException(String msg) {
        super(msg);
    }
}
