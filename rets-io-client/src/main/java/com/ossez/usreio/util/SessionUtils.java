package com.ossez.usreio.util;

import com.ossez.usreio.client.*;
import com.ossez.usreio.common.rets.RetsConfigurator;
import com.ossez.usreio.common.rets.RetsVersion;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SessionUtils for RETS server session
 *
 * @author YuCheng Hu
 */
public final class SessionUtils {
    private static final Logger logger = LoggerFactory.getLogger(SessionUtils.class);

    // Prevent the class from being constructed
    private SessionUtils() {

    }

    /**
     * @param retsConfigurator
     * @return
     * @throws RetsException
     */
    public static RetsSession retsLogin(RetsConfigurator retsConfigurator) throws RetsException {
        logger.debug("RETS Session Login URL: [{}]", retsConfigurator.getServerUrl());

        LoginResponse loginResponse = new LoginResponse();

        //Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
        RetsHttpClient httpClient = new CommonsHttpClient();

        // SET RETS VERSION
        if (ObjectUtils.isEmpty(retsConfigurator.getRetsVersion()))
            retsConfigurator.setRetsVersion(RetsVersion.DEFAULT);

        //Create a RetesSession with RetsHttpClient
        RetsSession session = new RetsSession(retsConfigurator.getServerUrl(), httpClient, retsConfigurator.getRetsVersion());

        //Set method as GET or POST
        session.setMethod("POST");
        try {
            //Login
            loginResponse = session.login(retsConfigurator.getServerUsername(), retsConfigurator.getServerPassword());
        } catch (RetsException ex) {
            throw ex;
        }

        // SESSION NULL CHECK
        if (!(!ObjectUtils.isEmpty(session) && StringUtils.isNotEmpty(loginResponse.getSessionId()))) {
            session = null;
        }

        logger.info("Session ID :[{}]", loginResponse.getSessionId());
        return session;
    }

    /**
     * Login to Server and return session Object
     *
     * @param retsLoginUrl
     * @param retsUsername
     * @param retsPassword
     * @return
     */
    public static RetsSession retsLogin(String retsLoginUrl, String retsUsername, String retsPassword, RetsVersion retsVersion) throws RetsException {
        logger.debug("RETS Session Login URL: [{}]", retsLoginUrl);

        LoginResponse loginResponse = new LoginResponse();

        //Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
        RetsHttpClient httpClient = new CommonsHttpClient();

        // SET RETS VERSION
        if (ObjectUtils.isEmpty(retsVersion))
            retsVersion = RetsVersion.DEFAULT;

        //Create a RetesSession with RetsHttpClient
        RetsSession session = new RetsSession(retsLoginUrl, httpClient, retsVersion);

        //Set method as GET or POST
        session.setMethod("POST");
        try {
            //Login
            loginResponse = session.login(retsUsername, retsPassword);
        } catch (RetsException ex) {
            throw ex;
        }

        // SESSION NULL CHECK
        if (!(!ObjectUtils.isEmpty(session) && StringUtils.isNotEmpty(loginResponse.getSessionId()))) {
            session = null;
        }

        logger.info("Session ID :[{}]", loginResponse.getSessionId());
        return session;
    }
}
