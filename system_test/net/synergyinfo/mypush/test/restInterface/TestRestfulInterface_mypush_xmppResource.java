package net.synergyinfo.mypush.test.restInterface;


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
import com.sun.jersey.core.util.Base64;

public class TestRestfulInterface_mypush_xmppResource extends TestAppInterface {
	private static final Logger LOGGER = Logger.getLogger(TestRestfulInterface_mypush_xmppResource.class);

	public TestRestfulInterface_mypush_xmppResource() {
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
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("devId", "08336a55b5f842e0aecbd9bc66455f22");
			j.put("data", Base64.encode("hello"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, j);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		String str = response.getEntity(String.class);
		LOGGER.info(str);
	}
	
	/**
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push_wrong_json() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		String wrongJson= "{}";

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, wrongJson);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		String str = response.getEntity(String.class);
		LOGGER.info(str);
		
		Assert.assertTrue("should return wrong input", str != null && str.contains("wrong input"));
	}
	
	/**
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push_wrong_json_devId_data() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("devId", "");
			j.put("data", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, j);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		String str = response.getEntity(String.class);
		LOGGER.info(str);
		
		Assert.assertTrue("should return wrong input", str != null && str.contains("wrong input"));
	}
	
	/**
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push_wrong_json_devId_data_2() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("devId_wrong", "123");
			j.put("data", "123");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, j);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		String str = response.getEntity(String.class);
		LOGGER.info(str);
		
		Assert.assertTrue("should return wrong input", str != null && str.contains("wrong input"));
	}
	
	/**
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push_wrong_json_devId_data_3() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("devId", "123");
			j.put("data_wrong", "123");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, j);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		String str = response.getEntity(String.class);
		LOGGER.info(str);
		
		Assert.assertTrue("should return wrong input", str != null && str.contains("wrong input"));
	}
	
	/**
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push_non_json() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		String non_json="{sdfa";

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, non_json);
		Assert.assertEquals("400", 400, response.getStatus());
	}
	
	/**
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push_correct_json_devId_data() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("devId", "08336a55b5f842e0aecbd9bc66455f22");
			j.put("data", Base64.encode("hello"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, j);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		String str = response.getEntity(String.class);
		LOGGER.info(str);
		
		Assert.assertTrue("should return finished", str != null && str.contains("finished"));
	}
	
	/**
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push_non_base64() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("devId", "08336a55b5f842e0aecbd9bc66455f22");
			j.put("data", "asljkdfl;asjd;lf");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, j);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		String str = response.getEntity(String.class);
		LOGGER.info(str);
		
		Assert.assertTrue("should return finished", str != null && str.contains("finished"));
	}
	
	/**
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push_check_return_json() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("devId", "08336a55b5f842e0aecbd9bc66455f22");
			j.put("data", "blah");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ClientResponse response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, j);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		String str = response.getEntity(String.class);
		LOGGER.info(str);
		
		Assert.assertTrue("should return finished", str != null && str.contains("finished"));
		
		JSONObject returnJson = null;
		try {
			returnJson= new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("return is not a Json");
		}
		if( returnJson != null ){
			try {
				returnJson.getString("status");
				returnJson.getString("msg");
				returnJson.getString("id");
			} catch (JSONException e) {
				e.printStackTrace();
				Assert.fail("response Json have wrong parameters");
			}
		}
	}
}
