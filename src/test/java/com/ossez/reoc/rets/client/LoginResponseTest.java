package com.ossez.reoc.rets.client;


import org.junit.Test;

public class LoginResponseTest extends RetsTestCase {
    /**
     * @throws RetsException
     */
    @Test
    public void testValidLoginResponse17() throws RetsException {
        LoginResponse response = new LoginResponse();
        response.parse(getResource("login_response_valid_1.7.xml"), RetsVersion.RETS_17);
        assertEquals("Checking broker", "4935,4935", response.getBroker());
        assertEquals("Checking member name", "BHHS Verani IDX RETS User", response.getMemberName());
        assertEquals("Checking metadata version", "19.9.17332", response.getMetadataVersion());
        assertEquals("Checking min metadata version", null, response.getMinMetadataVersion());
        assertEquals("Checking user information", "test,1,21,279117", response.getUserInformation());
        assertNull("Checking office list", response.getOfficeList());
        assertEquals("Checking balance", null, response.getBalance());
        assertEquals("Checking timeout", 7200, response.getSessionTimeout());
        assertNull("Checking password expiration", response.getPasswordExpiration());

        CapabilityUrls urls = response.getCapabilityUrls();
        assertEquals(null, urls.getActionUrl());
        assertEquals(null, urls.getChangePasswordUrl());
        assertEquals("http://neren.rets.paragonrels.com/rets/fnisrets.aspx/NEREN/getobject", urls.getGetObjectUrl());
        assertEquals("http://neren.rets.paragonrels.com/rets/fnisrets.aspx/NEREN/login", urls.getLoginUrl());
        assertNull(urls.getLoginCompleteUrl());
        assertEquals("http://neren.rets.paragonrels.com/rets/fnisrets.aspx/NEREN/logout", urls.getLogoutUrl());
        assertEquals("http://neren.rets.paragonrels.com/rets/fnisrets.aspx/NEREN/search", urls.getSearchUrl());
        assertEquals("http://neren.rets.paragonrels.com/rets/fnisrets.aspx/NEREN/getmetadata", urls.getGetMetadataUrl());
        assertNull(urls.getUpdateUrl());
    }


    /**
     * @throws RetsException
     */
    public void testValidLoginResponse15() throws RetsException {
        LoginResponse response = new LoginResponse();
        response.parse(getResource("login_response_valid_1.5.xml"), RetsVersion.RETS_15);
        assertEquals("Checking broker", "B123, BO987", response.getBroker());
        assertEquals("Checking member name", "Joe T. Schmoe", response.getMemberName());
        assertEquals("Checking metadata version", "1.00.000", response.getMetadataVersion());
        assertEquals("Checking min metadata version", "1.00.000", response.getMinMetadataVersion());
        assertEquals("Checking user information", "A123,5678,1,A123", response.getUserInformation());
        assertNull("Checking office list", response.getOfficeList());
        assertEquals("Checking balance", "44.21", response.getBalance());
        assertEquals("Checking timeout", 60, response.getSessionTimeout());
        assertNull("Checking password expiration", response.getPasswordExpiration());

        CapabilityUrls urls = response.getCapabilityUrls();
        assertEquals("http://rets.test:6103/get", urls.getActionUrl());
        assertEquals("http://rets.test:6103/changePassword", urls.getChangePasswordUrl());
        assertEquals("http://rets.test:6103/getObjectEx", urls.getGetObjectUrl());
        assertEquals("http://rets.test:6103/login", urls.getLoginUrl());
        assertNull(urls.getLoginCompleteUrl());
        assertEquals("http://rets.test:6103/logout", urls.getLogoutUrl());
        assertEquals("http://rets.test:6103/search", urls.getSearchUrl());
        assertEquals("http://rets.test:6103/getMetadata", urls.getGetMetadataUrl());
        assertNull(urls.getUpdateUrl());
    }

    /**
     * @throws RetsException
     */
    public void testValidLoginResponse10() throws RetsException {
        LoginResponse response = new LoginResponse();
        response.parse(getResource("login_response_valid_1.0.xml"), RetsVersion.RETS_10);
        assertEquals("Checking broker", "B123, BO987", response.getBroker());
        assertEquals("Checking member name", "Joe T. Schmoe", response.getMemberName());
        assertEquals("Checking metadata version", "1.00.000", response.getMetadataVersion());
        assertEquals("Checking min metadata version", "1.00.000", response.getMinMetadataVersion());
        assertEquals("Checking user information", "A123,5678,1,A123", response.getUserInformation());
        assertNull("Checking office list", response.getOfficeList());
        assertEquals("Checking balance", "44.21", response.getBalance());
        assertEquals("Checking timeout", 60, response.getSessionTimeout());
        assertNull("Checking password expiration", response.getPasswordExpiration());

        CapabilityUrls urls = response.getCapabilityUrls();
        assertEquals("http://rets.test:6103/get", urls.getActionUrl());
        assertEquals("http://rets.test:6103/changePassword", urls.getChangePasswordUrl());
        assertEquals("http://rets.test:6103/getObjectEx", urls.getGetObjectUrl());
        assertEquals("http://rets.test:6103/login", urls.getLoginUrl());
        assertNull(urls.getLoginCompleteUrl());
        assertEquals("http://rets.test:6103/logout", urls.getLogoutUrl());
        assertEquals("http://rets.test:6103/search", urls.getSearchUrl());
        assertEquals("http://rets.test:6103/getMetadata", urls.getGetMetadataUrl());
        assertNull(urls.getUpdateUrl());
    }

    public void testLowerCaseKeys() throws RetsException {
        LoginResponse response = new LoginResponse();
        response.parse(getResource("login_lower_case.xml"), RetsVersion.RETS_15);
        assertEquals("Checking broker", "B123, BO987", response.getBroker());
        assertEquals("Checking member name", "Joe T. Schmoe", response.getMemberName());
        assertEquals("Checking metadata version", "1.00.000", response.getMetadataVersion());
        assertEquals("Checking min metadata version", "1.00.000", response.getMinMetadataVersion());
        assertEquals("Checking user information", "A123,5678,1,A123", response.getUserInformation());
        assertNull("Checking office list", response.getOfficeList());
        assertEquals("Checking balance", "44.21", response.getBalance());
        assertEquals("Checking timeout", 60, response.getSessionTimeout());
        assertNull("Checking password expiration", response.getPasswordExpiration());

        CapabilityUrls urls = response.getCapabilityUrls();
        assertEquals("http://rets.test:6103/get", urls.getActionUrl());
        assertEquals("http://rets.test:6103/changePassword", urls.getChangePasswordUrl());
        assertEquals("http://rets.test:6103/getObjectEx", urls.getGetObjectUrl());
        assertEquals("http://rets.test:6103/login", urls.getLoginUrl());
        assertNull(urls.getLoginCompleteUrl());
        assertEquals("http://rets.test:6103/logout", urls.getLogoutUrl());
        assertEquals("http://rets.test:6103/search", urls.getSearchUrl());
        assertEquals("http://rets.test:6103/getMetadata", urls.getGetMetadataUrl());
        assertNull(urls.getUpdateUrl());
    }

    public void testStrictLowerCaseKeys() {
        LoginResponse response = new LoginResponse();
        response.setStrict(true);
        try {
            response.parse(getResource("login_lower_case.xml"), RetsVersion.RETS_15);

        } catch (RetsException e) {
            // Expected
            fail("Should throw exception");
        }
    }
}
