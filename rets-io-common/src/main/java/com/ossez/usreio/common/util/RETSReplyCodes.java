// $Header: /usr/local/cvsroot/rets/commons/src/main/java/org/realtor/rets/util/RETSReplyCodes.java,v 1.4 2005/04/06 12:44:06 ekovach Exp $
package com.ossez.usreio.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/**
 * Handles the mapping of replyCodes to replyText
 *
 * @author $Author: ekovach $
 * @version $Revision: 1.4 $
 */
public class RETSReplyCodes {
    /**
     * log4j Category object
     */
    private final static Logger logger = LoggerFactory.getLogger(RETSReplyCodes.class);

    /**
     * holds the Properties (Codes->Text) that are loaded at startup.
     */
    static Properties replyCodesProperties = null;

    static {
        try {
            replyCodesProperties = PropertiesLocator.locateProperties(
                    "RETSReplyCodes.properties");
        } catch (Exception e) {
            logger.error("Error Loading RETSReplyCodes.properties", e);
        }
    }

    /**
     * get the ReplyText mapped by the ReplyCode
     *
     * @param code error code
     * @return String of text mapped from code
     */
    static public String get(int code) {
        return replyCodesProperties.getProperty("" + code);
    }
}
