package net.synergyinfo.mypush.test.restInterface;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

/**
 * Utility class for some common operation in testing
 * 
 * @author ade
 * 
 */
public enum MyTestUtil {
	INSTANCE;
	
	public static final int randomTestCaseCount = 30;
	public static final int sparseTestCaseCount = 5;
	public static final int minTestCaseCount = 1;
	
	private Properties prop;
	private String path;
	private String prop_path;

	private Random myRandom = null;

	public static final String prop_package_path = "net/synergyinfo/mypush/test/restInterface/test.properties";
	public static final String prop_base_url = "baseUrl";

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
	
	/**
	 * This is a little tricky, find the correct stack trace number, this is related to the call stack!
	 * @return a method name
	 */
	public String getCurrentMethodName(final int stackIndex){
		String name = Thread.currentThread().getStackTrace()[stackIndex].getMethodName();
		return name;
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
	
	public String getTestString( final int minLength, final int maxLength ){
		return getRandomString(getEqualProbabilityRandomNumber(minLength, maxLength));
	}

	public String[] getTestStringArr1dm(final int minLength,
			final int maxLength, final int sampleCount) {
		ArrayList<String> list = new ArrayList<String>(sampleCount);
		for (int i = 0; i < sampleCount; i++) {
			list.add(getTestString( minLength, maxLength ));
		}
		String[] strArr = new String[sampleCount];
		return list.toArray(strArr);
	}
}
