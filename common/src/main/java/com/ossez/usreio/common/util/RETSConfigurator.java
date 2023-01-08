// $Header: /usr/local/cvsroot/rets/commons/src/main/java/org/realtor/rets/util/RETSConfigurator.java,v 1.2 2003/12/04 15:27:03 rsegelman Exp $
package com.ossez.usreio.common.util;




/**
 *        RETSConfigurator
 *                Singleton to limit number of times BasicConfigurator.configure is called.
 */
public class RETSConfigurator {
    static boolean configured = false;

    private RETSConfigurator() {
    }

    /** calls <code>BasicConfigurator.configure()</code> only once */
    static public void configure() {
        if (!configured) {
//            BasicConfigurator.configure();
            configured = true;
        }
    }
}
