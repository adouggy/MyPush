package net.synergyinfo.mypush.test.restInterface;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;


@RunWith(value = Parameterized.class)
public class TestRestfulInterface_mypush_xmppResource_user_get extends TestAppInterface {
	private static final Logger LOGGER = Logger.getLogger(TestRestfulInterface_mypush_xmppResource_user_get.class);

	private boolean isPositive = false;
	
	public TestRestfulInterface_mypush_xmppResource_user_get(
			final String isPositive) {
		super( "xmpp" );

		this.isPositive = Boolean.parseBoolean(isPositive);
	}
	
	int sizeForLoginLog = 0;
	int sizeForOpLog = 0;
	int sizeForSql = 0;

	@Parameters
	public static List<String[]> initialData() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		
		//positive test
		for( int i=0; i<MyTestUtil.randomTestCaseCount; i++ ){
			String[] cases = { 
					"true"
			};
			list.add(cases);
		}
		return list;
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
	 * Test /mypush/resources/xmpp/user[GET] 
	 */
	@Test
	public void test_user_get() {
		/**
		 * 先试试获得一个不存在的用户
		 */
		
		url = urlPrefix + "resources/" + resourcePath + "/" + "user/" +"1234";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		ClientResponse response = resource
				.type(MediaType.TEXT_HTML)
				.accept(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		Assert.assertEquals("200 ok", 200, response.getStatus());
		
		String str = response.getEntity(String.class);
		LOGGER.info(str);
		
		JSONObject returnJson = null;
		String status = null;
		try {
			returnJson= new JSONObject(str);
			status = returnJson.getString("status");
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("return is not a Json");
		}
		Assert.assertTrue("non-exists user, status = error", "error".compareTo(status) == 0);
		
		/**
		 * 注册一个用户
		 */
		url = urlPrefix + "resources/" + resourcePath + "/" + "register";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("username", "testUser");
			j.put("password", "Blah..");
			j.put("name", "Blah..");
			j.put("email", "Blah..");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		 response = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, j);
		Assert.assertEquals("200 ok", 200, response.getStatus());

		str = response.getEntity(String.class);
		LOGGER.info(str);
		
		returnJson = null;
		status = null;
		String id = null;
		String msg = null;
		try {
			returnJson= new JSONObject(str);
			status = returnJson.getString("status");
			id = returnJson.getString("id");
			msg = returnJson.getString("msg");
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("return is not a Json");
		}
		
		
		LOGGER.info("id=" + id);
		
		/**
		 * 再试试获得一个存在的用户
		 */
		url = urlPrefix + "resources/" + resourcePath + "/" + "user/" + id;
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		response = resource
				.type(MediaType.TEXT_HTML)
				.accept(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		Assert.assertEquals("200 ok", 200, response.getStatus());
		
		str = response.getEntity(String.class);
		LOGGER.info(str);
		
		returnJson = null;
		status = null;
		try {
			returnJson= new JSONObject(str);
			status = returnJson.getString("status");
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("return is not a Json");
		}
		Assert.assertTrue("exists user, status = ok", "ok".compareTo(status) == 0);
		
		/**
		 * 然后删除这个id！应该返回ok
		 */
		url = urlPrefix + "resources/" + resourcePath + "/" + "user/" + id;
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		response = resource
				.type(MediaType.TEXT_HTML)
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		Assert.assertEquals("200 ok", 200, response.getStatus());
		
		str = response.getEntity(String.class);
		LOGGER.info(str);
	}

}

