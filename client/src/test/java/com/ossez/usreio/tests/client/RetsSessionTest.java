package com.ossez.usreio.tests.client;

import com.ossez.usreio.client.RetsException;
import com.ossez.usreio.client.RetsSession;

import com.ossez.usreio.common.rets.RetsVersion;
import com.ossez.usreio.util.SessionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * RETS Connection Test
 *
 * @author YuCheng Hu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RetsSessionTest extends RetsTestCase {
    private final Logger logger = LoggerFactory.getLogger(RetsSessionTest.class);

    /**
     * Test Login should return SessionID from server
     */
    @Test
    public void testLogin() {
        logger.debug("Test Rets Session Login by URL: [{}]", retsLoginUrl);

        try {
            RetsSession session = SessionUtils.retsLogin(retsLoginUrl, retsUsername, retsPassword, RetsVersion.RETS_1_7_2);
            assertNotNull(session.getSessionId());

            // CHECK MetaData
            logger.error("Session ID - [{}]", session.getSessionId());
            logger.error("MetaData version - [{}]", session.getMetadataVersion());
            logger.error("MetaData Timestamp - [{}]", session.getMetadataTimestamp());

        } catch (RetsException ex) {
            logger.error("Session Login Error", ex);
        }
    }

    /**
     * Test Login should return SessionID from server
     */
    @Test
    public void testLoginWithConfigurator() {
        logger.debug("Test Rets Session Login by URL: [{}]", retsLoginUrl);

        try {
            RetsSession session = SessionUtils.retsLogin(retsConfigurator);
//            session.login()
            assertNotNull(session.getSessionId());
        } catch (RetsException ex) {
            logger.debug("Session Login Error", ex);
        }
    }


    /**
     * TEST Logout
     */
    @Test
    public void testLogout() {
        logger.debug("RETS Session Login URL: [{}]", retsLoginUrl);
        RetsSession session = null;

        try {
            session = SessionUtils.retsLogin(retsLoginUrl, retsUsername, retsPassword, RetsVersion.RETS_1_7_2);

            assertNotNull(session.getSessionId());

        } catch (RetsException ex) {
            logger.debug("Session Login Error", ex);
        }


        // If Session is not Empty then Logout
        if (ObjectUtils.isNotEmpty(session)) {
            try {
                session.logout();
            } catch (RetsException e) {
                logger.error("Logout Error: ", e);
            }
        }

    }

}
