package com.ossez.usreio.client;

import com.ossez.usreio.common.util.SessionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test for RETS session
 *
 * @author YuCheng Hu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RetsSessionTest extends RetsTestCase {
    private final Logger logger = LoggerFactory.getLogger(RetsSessionTest.class);


    /**
     * Test Login
     */
    @Test
    public void testLogin() {

        try {
            RetsSession session = SessionUtils.retsLogin(retsLoginUrl, retsUsername, retsPassword, RetsVersion.RETS_1_7_2);
            assertNull(session.getSessionId());
        } catch (RetsException ex) {
            logger.debug("Session Login Error", ex);
        }


    }

    /**
     * TEST Logout
     */
    @org.junit.jupiter.api.Test
    public void testLogout() {
        logger.debug("RETS Session Logout URL: [{}]", retsLoginUrl);
        RetsSession session = null;

        try {
            session = SessionUtils.retsLogin(retsLoginUrl, retsUsername, retsPassword, RetsVersion.RETS_1_7_2);
//            Assert.assertNotNull(session.getSessionId());
        } catch (RetsException ex) {
            logger.debug("Session Login Error", ex);
        }


        if (session != null) {
            try {
                session.logout();
            } catch (RetsException e) {
                e.printStackTrace();
            }
        }

    }


}
