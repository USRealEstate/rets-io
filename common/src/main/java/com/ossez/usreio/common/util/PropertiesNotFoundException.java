/* $Header: /usr/local/cvsroot/rets/commons/src/main/java/org/realtor/rets/util/PropertiesNotFoundException.java,v 1.2 2003/12/04 15:27:03 rsegelman Exp $  */
package com.ossez.usreio.common.util;


/**
 *  PropertiesNotFoundException.java Created Aug 6, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class PropertiesNotFoundException extends Exception {
    public PropertiesNotFoundException(String message) {
        super(message);
    }
}
