package com.ossez.usreio.tests.client;

import org.junit.jupiter.api.Test;
import com.ossez.usreio.client.retsapi.RETSConnection;
import com.ossez.usreio.client.retsapi.RETSLoginTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.InputStream;
import java.util.Properties;

/**
 * @author YuCheng
 */
public class ConnectionTest {

    private final static Logger logger = LoggerFactory.getLogger(ConnectionTest.class);

    /**
     * Do RetsServerConnection Test
     */
    @Test
    public void testStaticVariableChange() {

        //        BasicConfigurator.configure();

        RETSConnection rc = new RETSConnection();
        RETSLoginTransaction trans = new RETSLoginTransaction();

        try {
            Properties props = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream("rets.properties");

            props.load(inputStream);

            // Add the optional request parameters if they exist, are non-null and non-zero-length
            // rc.setRequestHeaderField("Authorization", (String)props.get("login.AUTHORIZATION"));
            rc.setServerUrl((String) props.getProperty("rets_server"));
            trans.setUrl((String) props.getProperty("rets_server"));
            trans.setUsername((String) props.getProperty("rets_username"));
            trans.setPassword((String) props.getProperty("rets_password"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        rc.execute(trans);

    }

}
