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
public class TestRestfulInterface_mypush_xmppResource_a_lot extends TestAppInterface {
	private static final Logger LOGGER = Logger.getLogger(TestRestfulInterface_mypush_xmppResource_a_lot.class);
	
	private int index;
	private boolean isPositive = false;
	
	public TestRestfulInterface_mypush_xmppResource_a_lot( final String indexStr, final String isPositive) {
		super( "xmpp" );
		this.index = Integer.parseInt(indexStr);
		this.isPositive = Boolean.parseBoolean(isPositive);
	}
	
	int sizeForLoginLog = 0;
	int sizeForOpLog = 0;
	int sizeForSql = 0;

	@Parameters
	public static List<String[]> initialData() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		for( int i=0; i<1000; i++ ){
			String[] cases = { 
					String.valueOf(i),
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
	public void test_push_check_response_status() {
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

