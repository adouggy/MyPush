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
public class TestRestfulInterface_mypush_xmppResource_devId_data extends TestAppInterface {
	private static final Logger LOGGER = Logger.getLogger(TestRestfulInterface_mypush_xmppResource_devId_data.class);
	
	private String devId;
	private String data;
	private boolean isPositive = false;
	
	public TestRestfulInterface_mypush_xmppResource_devId_data( final String devId, final String data, final String isPositive) {
		super( "xmpp" );
		this.devId = devId;
		this.data = data;
		this.isPositive = Boolean.parseBoolean(isPositive);
	}
	
	int sizeForLoginLog = 0;
	int sizeForOpLog = 0;
	int sizeForSql = 0;

	@Parameters
	public static List<String[]> initialData() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		//case for large devId
		for( int i=0; i<MyTestUtil.randomTestCaseCount; i++ ){
			String[] cases = { 
					MyTestUtil.INSTANCE.getTestString(10000, 100000),
					"data",
					"true"
			};
			list.add(cases);
		}
		
		//caes for large data
		for( int i=0; i<MyTestUtil.randomTestCaseCount; i++ ){
			String[] cases = { 
					"non_exists_devId",
					MyTestUtil.INSTANCE.getTestString(50000, 100000),
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
	 * Test /mypush/resources/xmp/push[POST] 
	 */
	@Test
	public void test_push_check_devId_data() {
		url = urlPrefix + "resources/" + resourcePath + "/" + "push";
		LOGGER.info(MyTestUtil.INSTANCE.getCurrentMethodName(2));
		LOGGER.info(url);
		client = new Client();
		resource = client.resource(url);
		
		JSONObject j = new JSONObject();
		try {
			j.put("devId", this.devId);
			j.put("data", this.data);
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
		
		String status = null, msg = null, id = null;
		if( returnJson != null ){
			try {
				status = returnJson.getString("status");
				msg = returnJson.getString("msg");
				id = returnJson.getString("id");
			} catch (JSONException e) {
				e.printStackTrace();
				Assert.fail("response Json have wrong parameters");
			}
		}
		
		Assert.assertEquals("Response status should be ok..", status, "ok");
		Assert.assertEquals("Response msg should be Sent..", msg, "finished");
		
	}

}

