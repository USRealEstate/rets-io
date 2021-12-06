package com.ossez.usreio.tests.client;

import com.ossez.usreio.client.RetsException;
import com.ossez.usreio.client.RetsSession;
import com.ossez.usreio.client.SearchRequest;
import com.ossez.usreio.client.SearchResultImpl;
import com.ossez.usreio.client.retsapi.RETSConnection;
import com.ossez.usreio.client.retsapi.RETSGetMetadataTransaction;
import com.ossez.usreio.tests.common.metadata.types.MClass;
import com.ossez.usreio.tests.common.metadata.types.MResource;
import com.ossez.usreio.tests.common.metadata.types.MSystem;
import com.ossez.usreio.util.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Simple Example performing a GetMetadata and iterating of the results
 *
 * @author YuCheng Hu
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RetsListingTest extends RetsTestCase {

    private final Logger logger = LoggerFactory.getLogger(RetsListingTest.class);

    @Test
    public void testGetRetsMetadata() {
        RetsSession session = null;
        try {

            session = SessionUtils.retsLogin(retsConfigurator);

            String sQuery = "(Member_num=.ANY.)";
            String sResource = "Property";
            String sClass = "RE_1";

            //Create a SearchRequest
            SearchRequest request = new SearchRequest(sResource, sClass, sQuery);

            //Select only available fields
            String select = "L_ListingID,L_Address";
            request.setSelect(select);

            //Set request to retrive count if desired
            request.setCountFirst();

            SearchResultImpl response;


            //Execute the search
            response = (SearchResultImpl) session.search(request);

            //Print out count and columns
            int count = response.getCount();
            System.out.println("COUNT: " + count);
            System.out.println("COLUMNS: " + StringUtils.join(response.getColumns(), "\t"));

            //Iterate over, print records
            for (int row = 0; row < response.getRowCount(); row++) {
                System.out.println("ROW" + row + ": " + StringUtils.join(response.getRow(row), "\t"));
            }


        } catch (RetsException e) {
            logger.error("RETS Request Error.", e);
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

}