/* $Header: /usr/local/cvsroot/rets/commons/src/main/java/org/realtor/rets/util/PropertiesLocator.java,v 1.2 2003/12/04 15:27:03 rsegelman Exp $  */
package com.ossez.usreio.common.util;

import java.io.IOException;

import java.util.Properties;


/**
 *  PropertiesLocator.java Created Aug 6, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class PropertiesLocator {
    public static Properties locateProperties(String fileName)
        throws PropertiesNotFoundException {
        ClassLoader loader = PropertiesLocator.class.getClassLoader();
        Properties p = new Properties();

        try {
            p.load(loader.getResourceAsStream(fileName));
        } catch (IOException e) {
            PropertiesNotFoundException nfe = new PropertiesNotFoundException(
                    "Could not find file " + fileName);
            nfe.fillInStackTrace();
            throw nfe;
        }

        return p;
    }
}
