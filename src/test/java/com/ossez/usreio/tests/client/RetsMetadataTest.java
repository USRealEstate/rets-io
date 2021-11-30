package com.ossez.usreio.tests.client;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

import com.ossez.usreio.client.retsapi.RETSConnection;
import com.ossez.usreio.client.retsapi.RETSGetMetadataTransaction;
import com.ossez.usreio.tests.common.metadata.types.MClass;
import com.ossez.usreio.tests.common.metadata.types.MResource;
import com.ossez.usreio.tests.common.metadata.types.MSystem;
import com.ossez.usreio.client.*;
import org.junit.jupiter.api.Test;

/**
 * Simple Example performing a GetMetadata and iterating of the results
 */
public class RetsMetadataTest {

    public static void main(String[] args) throws MalformedURLException {

        //Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
        RetsHttpClient httpClient = new CommonsHttpClient();
        RetsVersion retsVersion = RetsVersion.RETS_1_7_2;
        String loginUrl = "";

        //Create a RetesSession with RetsHttpClient
        RetsSession session = new RetsSession(loginUrl, httpClient, retsVersion);

        String username = "";
        String password = "";

        //Set method as GET or POST
        session.setMethod("POST");
        try {
            //Login
            LoginResponse loginResponse = session.login(username, password);

            System.out.println(">>>" + loginResponse.getSessionId());
        } catch (RetsException e) {
            e.printStackTrace();
        }

        try {
            MSystem system = session.getMetadata().getSystem();
            System.out.println(
                    "SYSTEM: " + system.getSystemID() +
                            " - " + system.getSystemDescription());

            for (MResource resource : system.getMResources()) {

                System.out.println(
                        "    RESOURCE: " + resource.getResourceID());

                for (MClass classification : resource.getMClasses()) {
                    System.out.println(
                            "        CLASS: " + classification.getClassName() +
                                    " - " + classification.getDescription());
                }
            }
        } catch (RetsException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                try {
                    session.logout();
                } catch (RetsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Do RetsServerConnection Test
     */
    @Test
    public void testStaticVariableChange() {

        //        BasicConfigurator.configure();

        RETSConnection rc = new RETSConnection();
//        RETSLoginTransaction trans = new RETSLoginTransaction();
        RETSGetMetadataTransaction trans = new RETSGetMetadataTransaction();


        try {
            Properties props = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream("rets.properties");

            props.load(inputStream);

            // Add the optional request parameters if they exist, are non-null and non-zero-length
            // rc.setRequestHeaderField("Authorization", (String)props.get("login.AUTHORIZATION"));
            rc.setServerUrl((String) props.getProperty("rets_server"));
//            trans.setUrl((String) props.getProperty("rets_server"));
//            trans.setUsername((String) props.getProperty("rets_username"));
//            trans.setPassword((String) props.getProperty("rets_password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rc.execute(trans);
//        rc.execute(transaction);

//        transaction.getVersion();

    }
}
