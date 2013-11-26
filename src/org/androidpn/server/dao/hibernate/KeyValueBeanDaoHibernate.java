package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.KeyValueBeanDao;
import org.androidpn.server.model.KeyValueBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * implements KeyValueBeanDao interface 
 * @author ade
 *
 */
public class KeyValueBeanDaoHibernate extends HibernateDaoSupport implements KeyValueBeanDao {

	@Override
    public KeyValueBean getKeyValueBean(Long id) {
        return (KeyValueBean) getHibernateTemplate().get(KeyValueBean.class, id);
    }

	@Override
    public KeyValueBean saveKeyValueBean(KeyValueBean bean) {
        getHibernateTemplate().saveOrUpdate(bean);
        getHibernateTemplate().flush();
        return bean;
    }

	@Override
    public void removeKeyValueBean(Long id) {
        getHibernateTemplate().delete(getKeyValueBean(id));
    }

	@Override
    public boolean exists(Long id) {
    	KeyValueBean bean = (KeyValueBean) getHibernateTemplate().get(KeyValueBean.class, id);
        return bean != null;
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<KeyValueBean> getKeyValueBean() {
        return getHibernateTemplate().find(
                "from KeyValueBean u order by u.id desc");
    }

}
