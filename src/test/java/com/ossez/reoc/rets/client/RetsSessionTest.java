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

        RetsSession session = SessionUtils.retsLogin(retsLoginUrl, retsUsername, retsPassword, RetsVersion.RETS_1_7_2);
        Assert.assertNotNull(session.getSessionId());

    }

    /**
     * TEST Logout
     */
    public void testLogout() {
        logger.debug("RETS Session Logout URL: [{}]", retsLoginUrl);
        RetsSession session = SessionUtils.retsLogin(retsLoginUrl, retsUsername, retsPassword, RetsVersion.RETS_1_7_2);
        Assert.assertNotNull(session.getSessionId());

        if (session != null) {
            try {
                session.logout();
            } catch (RetsException e) {
                e.printStackTrace();
            }
        }
        Assert.assertNull(session.getSessionId());

    }


}
