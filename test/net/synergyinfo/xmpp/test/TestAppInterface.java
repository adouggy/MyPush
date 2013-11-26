package net.synergyinfo.xmpp.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public abstract class TestAppInterface {
	private static final Logger LOGGER = Logger.getLogger(TestAppInterface.class);
	protected static final String urlPrefix = MyTestUtil.getInstance().getProperty(MyTestUtil.prop_base_url);
	protected String resourcePath = null;
	protected String url = null;
	protected WebResource resource = null;
	protected Client client = null;

	public TestAppInterface( String resourceStr ) {
		LOGGER.debug("Test resource for:" + resourceStr);
		this.resourcePath = resourceStr;
	}

	@Before
	abstract public void before();

	@After
	abstract public void after();
}
