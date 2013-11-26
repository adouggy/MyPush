package net.synergyinfo.xmpp.test;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class TestRestfulInterface_xmpp_push extends TestAppInterface {
	private static final Logger LOGGER = Logger.getLogger(TestRestfulInterface_xmpp_push.class);

	public TestRestfulInterface_xmpp_push() {
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
	 * Test /MyPush/resources/xmpp/test[GET] 
	 */
	@Ignore
	@Test
	public void test_hello() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "test";
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);

		ClientResponse response = resource
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.TEXT_HTML)
				.get(ClientResponse.class);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		String str = response.getEntity(String.class);
		LOGGER.info("test_hello passed");
		LOGGER.debug(str);
	}
	
	/**
	 * Test /MyPush/resources/xmpp/push[POST] 
	 */
	@Ignore
	@Test
	public void test_push_negative() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		JSONObject json = new JSONObject();
		try {
			json.put("blah", "xxx");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		LOGGER.info("request:" + json.toString());
		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, json);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		JSONObject j = response.getEntity(JSONObject.class);
		
		
		LOGGER.debug(j.toString());
		try {
			Assert.assertEquals(j.get("status"), "Error");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LOGGER.info("test_push_negative passed");
	}
	
	@Test
	public void test_push() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		JSONObject json = new JSONObject();
		try {
			json.put("devId", "");
			try {
				json.put("data", new String(Base64.encode("blah...".getBytes("utf-8")), "utf-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		LOGGER.info("request:" + json.toString());
		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, json);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		JSONObject j = response.getEntity(JSONObject.class);
		
		
		LOGGER.debug(j.toString());
		try {
			Assert.assertEquals(j.get("status"), "ok");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LOGGER.info("test_push passed");
	}
}
