package net.synergyinfo.xmpp.test;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class TestRestfulInterface_xmpp_register extends TestAppInterface {
	private static final Logger LOGGER = Logger.getLogger(TestRestfulInterface_xmpp_register.class);

	public TestRestfulInterface_xmpp_register() {
		super( "xmpp" );
	}

	@Override
	@Before
	public void before() {
	}

	@Override
	@After
	public void after() {

	}

	/**
	 * Test /MyPush/resources/xmpp/register[POST] 
	 */
	@Test
	public void test_register() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "register";
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("username", "ade1");
			j.put("password", "password");
			j.put("email", "ade@ade.com");
			j.put("name", "my Name");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, j);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		JSONObject resJ = response.getEntity(JSONObject.class);
		LOGGER.info("test_register passed");
		LOGGER.debug(resJ.toString());
	}
	

}
