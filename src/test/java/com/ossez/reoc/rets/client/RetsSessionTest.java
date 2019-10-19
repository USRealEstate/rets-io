package com.ossez.reoc.rets.client;

import com.ossez.reoc.rets.common.util.SessionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test for RETS session
 *
 * @author YuCheng Hu
 */
public class RetsSessionTest extends RetsTestCase {
    private final Logger logger = LoggerFactory.getLogger(RetsSessionTest.class);

    /**
     * Test Login
     */
    @Test
    public void testLogin() {

        try {
            RetsSession session = SessionUtils.retsLogin(retsLoginUrl, retsUsername, retsPassword, RetsVersion.RETS_1_7_2);
            Assert.assertNotNull(session.getSessionId());
        } catch (RetsException ex) {
            logger.debug("Session Login Error", ex);
        }


    }

    /**
     * TEST Logout
     */
    public void testLogout() {
        logger.debug("RETS Session Logout URL: [{}]", retsLoginUrl);
        RetsSession session = null;

        try {
            session = SessionUtils.retsLogin(retsLoginUrl, retsUsername, retsPassword, RetsVersion.RETS_1_7_2);
            Assert.assertNotNull(session.getSessionId());
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
