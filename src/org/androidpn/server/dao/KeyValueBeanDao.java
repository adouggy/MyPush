package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.KeyValueBean;

/**
 * DAO interface for KeyValueBean
 * 
 * @author ade
 *
 */
public interface KeyValueBeanDao {

	public KeyValueBean getKeyValueBean( Long id );
	public KeyValueBean saveKeyValueBean( KeyValueBean bean );
	public void removeKeyValueBean( Long id );
	public boolean exists( Long id );
	public List<KeyValueBean> getKeyValueBean();
}
