package org.androidpn.server.dao.hibernate;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.OnlineUserCountDao;
import org.androidpn.server.model.OnlineUserCount;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class OnlineUserCountDaoHibernate extends HibernateDaoSupport implements OnlineUserCountDao{



	@Override
	public OnlineUserCount saveRecord(OnlineUserCount record) {
		this.getHibernateTemplate().save(record);
		return record;
	}

	@Override
	public OnlineUserCount getRecord(Date time) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OnlineUserCount> getAllRecords(Date startTime, Date endTime, String orderColumn, Order order) {
		DateFormat  df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		String sql = "from OnlineUserCount r";
		if(startTime != null || endTime != null) sql += " where ";
		if(startTime != null) sql += " r.time > '" + df.format(startTime) + "'";
		if(startTime != null && endTime != null) sql += " and ";
		if(endTime != null) sql += " r.time < '" + df.format(endTime) + "'";
		if(orderColumn != null && order != null) sql += " order by r." + orderColumn + " " + order;

		return this.getHibernateTemplate().find(sql);
	}

	

	@SuppressWarnings({ "unchecked" })
	private List<Object> executeSql(final String sql) {
		List<Object> list = getHibernateTemplate().executeFind(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query=session.createQuery(sql);
				List<Object> list = query.list();
				return list;
			}			
		});
		return list;
	}
	
}
