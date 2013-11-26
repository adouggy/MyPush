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
public class TestRestfulInterface_mypush_xmppResource_user_delete extends TestAppInterface {
	private static final Logger LOGGER = Logger.getLogger(TestRestfulInterface_mypush_xmppResource_user_delete.class);
	
	private String username;
	private String password;
	private String email;
	private String name;
	private boolean isPositive = false;
	
	public TestRestfulInterface_mypush_xmppResource_user_delete( 
			final String username, 
			final String password,
			final String email,
			final String name,
			final String isPositive) {
		super( "xmpp" );
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
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
					"test_" + MyTestUtil.INSTANCE.getTestString(5, 32),
					"test_" + MyTestUtil.INSTANCE.getTestString(5, 32),
					"name",
					"email",
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
	 * Test /mypush/resources/xmpp/user[DELETE] 
	 */
	@Test
	public void test_register_and_delete() {
		
		/**
		 * 
		 * ������ע��һ���û�
		 * 
		 */
		url = urlPrefix + "resources/" + resourcePath + "/" + "register";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("username", this.username);
			j.put("password", this.password);
			j.put("name", this.name);
			j.put("email", this.email);
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
		
		
		JSONObject returnJson = null;
		String status = null;
		String id = null;
		try {
			returnJson= new JSONObject(str);
			status = returnJson.getString("status");
			id = returnJson.getString("id");
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("return is not a Json");
		}
		
		
		LOGGER.info("id=" + id);
		
		/**
		 * Ȼ��ɾ�����id��Ӧ�÷���ok
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
		 * Ȼ����ɾ�����id��Ӧ�÷���error
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
		returnJson = null;
		status = null;
		try {
			returnJson= new JSONObject(str);
			status = returnJson.getString("status");
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("return is not a Json");
		}
		Assert.assertTrue("non-exists user, status = error", "error".compareTo(status) == 0);
	}

}

