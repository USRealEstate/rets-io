package com.ossez.usreio.tests.client;

import java.io.InputStream;
import java.util.Properties;

import com.ossez.usreio.client.retsapi.RETSConnection;
import com.ossez.usreio.client.retsapi.RETSGetMetadataTransaction;
import com.ossez.usreio.common.rets.RetsVersion;
import com.ossez.usreio.tests.common.metadata.types.MClass;
import com.ossez.usreio.tests.common.metadata.types.MResource;
import com.ossez.usreio.tests.common.metadata.types.MSystem;
import com.ossez.usreio.client.*;
import com.ossez.usreio.util.SessionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * Simple Example performing a GetMetadata and iterating of the results
 *
 * @author YuCheng Hu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RetsMetadataTest extends RetsTestCase {

    @Test
    public void testGetRetsMetadata() {
        RetsSession session = null;
        try {

            session = SessionUtils.retsLogin(retsConfigurator);

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
