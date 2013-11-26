package org.androidpn.server.console.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.dao.KeyValueBeanDao;
import org.androidpn.server.model.KeyValueBean;
import org.androidpn.server.xmpp.XmppServer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * A controller class to process synchronize demo requests
 * 
 * @author ade
 */
public class SyncDemoController extends MultiActionController {

	
	private KeyValueBeanDao keyValueBeanDao;


	public KeyValueBeanDao getKeyValueBeanDao() {
		return keyValueBeanDao;
	}


	public void setKeyValueBeanDao(KeyValueBeanDao keyValueBeanDao) {
		this.keyValueBeanDao = keyValueBeanDao;
	}


	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println( ">>> listing sync demo database." );
		keyValueBeanDao = (KeyValueBeanDao) XmppServer.getInstance().getBean("keyValueBeanDao");
		List<KeyValueBean> beans = keyValueBeanDao.getKeyValueBean();
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("list", beans);
		
		
		
		System.out.println( keyValueBeanDao );
		
		mav.setViewName("sync/db");
		return mav;
	}

}
