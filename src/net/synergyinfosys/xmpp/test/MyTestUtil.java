package net.synergyinfosys.xmpp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Utility class for some common operation in testing
 * 
 * @author ade
 * 
 */
public class MyTestUtil {
	public static final int randomTestCaseCount = 30;
	public static final int sparseTestCaseCount = 5;
	public static final int minTestCaseCount = 1;
	
	private static MyTestUtil _instance;

	private Properties prop;
	private String path;
	private String prop_path;
	
	private static String temp_prop_path;

	private Random myRandom = null;

	public static final String prop_package_path = "net/synergyinfosys/xmpp/test/test.properties";
	
	public static final String prop_base_url = "baseUrl";

	public static MyTestUtil getInstance() {
		if (_instance == null) {
			synchronized (MyTestUtil.class) {
				if (_instance == null)
					_instance = new MyTestUtil();
			}
		}
		return _instance;
	}
	
	private MyTestUtil() {
		path = MyTestUtil.class.getResource("/").toString().split(":")[1];

		prop_path = path + prop_package_path;

		this.reloadProperties();
		myRandom = new Random();
	}

	public void reloadProperties() {
		prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(prop_path));
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static String getTempPropPath(){
		MyTestUtil.getInstance();
		return temp_prop_path;
	}
	
	
	public static void saveTempProperty(String key, String value){
		Properties tempProp = new Properties();
		tempProp = MyTestUtil.getProperties(MyTestUtil.getTempPropPath());
		String ids = tempProp.getProperty(key, "");
		value = ids + ((ids.equals("")?"":",")) + value;
		tempProp.put(key, value);
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(MyTestUtil.getTempPropPath()));
			tempProp.store( fos, "test_add ids" );
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if( fos != null )
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	
	public static String getTempProperty(String key){
		Properties tempProp = new Properties();
		tempProp = MyTestUtil.getProperties(MyTestUtil.getTempPropPath());

		return tempProp.getProperty(key);
		
	}
	
	public static void deleteTempProperty(String key, String value){
		String ids = MyTestUtil.getTempProperty(key);
		String[] idArr = ids.split(",");
		
		String[] del_idArr = value.split(",");
		
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> del_list = new ArrayList<String>();
		
		for( int i=0; i<idArr.length; i++ ){
			list.add( idArr[i] );
		}
		
		for( int i=0; i<del_idArr.length; i++){
			del_list.add(del_idArr[i]);
		}
		
		list.removeAll(del_list);
		
		String str = "";
		
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			str = str + iter.next() + ","; 
		}
		
		if(str.length()>=1) {
			str =str.substring(0, str.length()-1);
		}
		
		Properties tempProp = MyTestUtil.getProperties(MyTestUtil.getTempPropPath());
		tempProp.setProperty(key, str);
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(MyTestUtil.getTempPropPath()));
			tempProp.store( fos, "test_add ids" );
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if( fos != null )
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
	public static Properties getProperties(String fileName){
		Properties tempProp = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(fileName));
			tempProp.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return tempProp;
	}
	
	
	

	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	public void setProperty(String key, String value) {
		prop.setProperty(key, value);
	}
	
	public Client getUnauthorizedClient(){
		return new Client();
	}

	public Client getAuthorizedClient(String resourceUrl) {
		ClientConfig conf = new DefaultClientConfig();
		Client client = Client.create(conf);
		//LoggingFilter lf = new LoggingFilter();
		//client.addFilter(lf);
		client.addFilter(new ClientFilter() {
			private ArrayList<Object> cookies;

			@Override
			public ClientResponse handle(ClientRequest request)
					throws ClientHandlerException {
				if (cookies != null) {
					request.getHeaders().put("Cookie", cookies);
				}
				ClientResponse response = getNext().handle(request);
				if (response.getCookies() != null) {
					if (cookies == null) {
						cookies = new ArrayList<Object>();
					}
					cookies.addAll(response.getCookies());
				}
				return response;
			}
		});
		
		//touch the resource first
		WebResource beginResource = client.resource(UriBuilder.fromUri(resourceUrl)
				.build());
		ClientResponse beginResponse = beginResource.type(MediaType.TEXT_PLAIN)
				.head();//.get(ClientResponse.class);
		Assert.assertEquals("should be 200 ok", 200,  beginResponse.getStatus() );

		// got cookie via filter and then login.
		String authUrl = MyTestUtil.getInstance().getProperty(MyTestUtil.prop_base_url) + "j_acegi_security_check";
//		System.out.println(authUrl);
		WebResource webResource = client.resource( authUrl );
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("j_username",
				MyTestUtil.getInstance().getProperty("username"));
		queryParams.add("j_password",
				MyTestUtil.getInstance().getProperty("password"));
		ClientResponse auth_Response = webResource.queryParams(queryParams)
				.type("application/x-www-form-urlencoded")
				.head();//(ClientResponse.class);
//		MultivaluedMap<String, String> mvp = auth_Response.getHeaders();
//		String auth_str = auth_Response.getEntity(String.class);

		Assert.assertEquals("should be 200ok", 200, auth_Response.getStatus());
//		System.out.println(auth_Response.getEntity(String.class));
//		System.out.println(mvp.getFirst("Content-Length"));
//		System.out.println(mvp.getFirst("Cookie"));
//		System.out.println(auth_str);
		
//		Iterator<String> iter = mvp.keySet().iterator();
//		while( iter.hasNext() ){
//			String key = iter.next();
//			System.out.println( key + "->" + mvp.getFirst(key) );
//		}
		
		//Assert.assertTrue(condition)
		
		return client;
	}

	public String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = this.myRandom.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public int getEqualProbabilityRandomNumber(int min, int max) {
		if (max == min) {
			return max;
		} else if (max < min) {
			int tmp = max;
			max = min;
			min = tmp;
		}
		return this.myRandom.nextInt(max - min) + min;
	}

	public int[] getTestIntArr(final int min, final int max,
			final int sampleCount) {
		int[] intArr = new int[sampleCount];
		for (int i = 0; i < sampleCount; i++) {
			intArr[i] = getEqualProbabilityRandomNumber(min, max);
		}

		return intArr;
	}

	public String[] getTestStringArr1dm(final int minLength,
			final int maxLength, final int sampleCount) {
		ArrayList<String> list = new ArrayList<String>(sampleCount);
		for (int i = 0; i < sampleCount; i++) {
			list.add(getRandomString(getEqualProbabilityRandomNumber(minLength,
					maxLength)));
		}
		String[] strArr = new String[sampleCount];
		return list.toArray(strArr);
	}

	public static Connection getConnection() throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://192.168.0.200:13306/edoc";
		Connection conn = DriverManager.getConnection(url, "root", "root");
		System.out.println("database Successfully opened.----");
		return conn;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println("path:"+MyTestUtil.getInstance().path);
//		System.out.println("prop_path:"+MyTestUtil.getInstance().prop_path);
	}
}
