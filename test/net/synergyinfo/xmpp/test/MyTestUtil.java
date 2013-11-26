package net.synergyinfo.xmpp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
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

	private Random myRandom = null;

	public static final String prop_package_path = "net/synergyinfo/xmpp/test/test.properties";
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

	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	public void setProperty(String key, String value) {
		prop.setProperty(key, value);
	}
	
	public Client getUnauthorizedClient(){
		return new Client();
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
}
