package org.androidpn.server.dao.hibernate;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.OnlineRecordDao;
import org.androidpn.server.model.OnlineRecord;
import org.androidpn.server.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class OnlineRecordDaoHibernate extends HibernateDaoSupport implements OnlineRecordDao{

	@Override
	public OnlineRecord getOnlineRecord(Long recordId) {
		OnlineRecord record = (OnlineRecord) getHibernateTemplate().get(OnlineRecord.class, recordId);
		return record;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OnlineRecord> getOnlineRecordsByUser(User user) {
		String sql = "from OnlineRecord r where r.user.id = " + user.getId() + " order by r.time desc";
		return getHibernateTemplate().find(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OnlineRecord> getOnlineRecordsByAction(String action) {
		String sql = "from OnlineRecord r where r.action = '" + action + "' order by r.time desc";
		return getHibernateTemplate().find(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OnlineRecord> getOnlineRecords(User user, String action) {
		String sql = "from OnlineRecord r where r.user.id = " + user.getId() + " and r.action = '" + action 
				+ "' order by r.time desc";
		return getHibernateTemplate().find(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OnlineRecord> getOnlineRecords(User user, Date startTime, Date endTime, String orderColumn, Order order) {
		DateFormat  df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		String sql = "from OnlineRecord r where " +
				"r.user.id = " + user.getId() + "";
		if(startTime != null) sql += " and r.time > '" + df.format(startTime) + "'";
		if(endTime != null) sql += " and r.time < '" + df.format(endTime) + "'";
		if(orderColumn != null && order != null) sql += " order by r." + orderColumn + " " + order;

		return getHibernateTemplate().find(sql);
	}
	
	@Override
	public OnlineRecord saveOnlineRecord(OnlineRecord record) {
		getHibernateTemplate().saveOrUpdate(record);
		getHibernateTemplate().flush();
		return record;
	}

	@Override
	public void removeOnlineRecord(OnlineRecord record) {
		getHibernateTemplate().delete(record);
		
	}

	@Override
	public void removeOnlineRecordByUser(User user) {
		getHibernateTemplate().deleteAll(this.getOnlineRecordsByUser(user));
		
	}

	@Override
	public int getLoginCount(User user) {
		final String sql = "SELECT COUNT(*) from OnlineRecord r where " +
				"r.user.id = " + user.getId() + "and r.action = 'login'";	
		int count = Integer.valueOf(this.executeSql(sql).get(0).toString());
		return count;
	}

	@Override
	public int getLoginCount(User user, Date startTime, Date endTime) {
		DateFormat  df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		String sql = "SELECT COUNT(*) from OnlineRecord r where " +
				"r.action = 'login' and r.user.id = " + user.getId() + "";
		if(startTime != null) sql += " and r.time > '" + df.format(startTime) + "'";
		if(endTime != null) sql += " and r.time < '" + df.format(endTime) + "'";

		int count = Integer.valueOf(this.executeSql(sql).get(0).toString());
		return count;
	}
	
	@Override
	public Long getOnlineTotalTime(User user) {
		//TODO 	
		return null;
	}

	@Override
	public Date getLastLoginTime(User user) {
		List<OnlineRecord> list = this.getOnlineRecords(user, OnlineRecord.Action.login.toString());
		if(list == null || list.size() <= 0) {
			return null;
		}
		return list.get(0).getTime();
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
