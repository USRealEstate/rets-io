package com.ossez.reoc.rets.examples;

import java.net.MalformedURLException;

import org.apache.commons.lang.StringUtils;
import com.ossez.reoc.rets.client.CommonsHttpClient;
import com.ossez.reoc.rets.client.RetsException;
import com.ossez.reoc.rets.client.RetsHttpClient;
import com.ossez.reoc.rets.client.RetsSession;
import com.ossez.reoc.rets.client.RetsVersion;
import com.ossez.reoc.rets.client.SearchRequest;
import com.ossez.reoc.rets.client.SearchResultImpl;

/**
 * Simple Example performing a search and iterating over the results
 *
 */
public class RetsSearchExample {

	public static void main(String[] args) throws MalformedURLException {

		//Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
		RetsHttpClient httpClient = new CommonsHttpClient();
		RetsVersion retsVersion = RetsVersion.RETS_1_7_2;
		String loginUrl = "http://neren.rets.paragonrels.com/rets/fnisrets.aspx/NEREN/login";

		//Create a RetesSession with RetsHttpClient
		RetsSession session = new RetsSession(loginUrl, httpClient, retsVersion);    

		String username = "prurets1";
		String password = "boyd070110";

		//Set method as GET or POST
		session.setMethod("POST");
		try {
			//Login
			session.login(username, password);
		} catch (RetsException e) {
			e.printStackTrace();
		}

		String sQuery = "(Member_num=.ANY.)";
		String sResource = "Property";
		String sClass = "Residential";

		//Create a SearchRequest
		SearchRequest request = new SearchRequest(sResource, sClass, sQuery);

		//Select only available fields
		String select ="field1,field2,field3,field4,field5";
		request.setSelect(select);

		//Set request to retrive count if desired
		request.setCountFirst();

		SearchResultImpl response;
		try {
			//Execute the search
			response= (SearchResultImpl) session.search(request);

			//Print out count and columns
			int count = response.getCount();
			System.out.println("COUNT: " + count);
			System.out.println("COLUMNS: " + StringUtils.join(response.getColumns(), "\t"));

			//Iterate over, print records
			for (int row = 0; row < response.getRowCount(); row++){
				System.out.println("ROW"+ row +": " + StringUtils.join(response.getRow(row), "\t"));
			}
		} catch (RetsException e) {
			e.printStackTrace();
		} 
		finally {
			if(session != null) { 
				try {
					session.logout(); 
				} 
				catch(RetsException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
