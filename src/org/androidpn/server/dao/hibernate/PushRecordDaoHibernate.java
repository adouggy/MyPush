package org.androidpn.server.dao.hibernate;

import java.util.List;

import net.synergyinfosys.xmpp.bean.PushStatus;

import org.androidpn.server.dao.PushRecordDao;
import org.androidpn.server.model.Message;
import org.androidpn.server.model.PushRecord;
import org.androidpn.server.model.User;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PushRecordDaoHibernate extends HibernateDaoSupport implements PushRecordDao {

	@Override
	public PushRecord getPushRecord(Long recordId) {
		PushRecord pr = (PushRecord) this.getHibernateTemplate().get(PushRecord.class, recordId);
		return pr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PushRecord> getAllPushRecords(User user, Message message, PushStatus status) {
		String hql = "from PushRecord r where r.user.id = " + user.getId() 
				+ " and r.message.id = '" + message.getId()
				+ " and r.status = '" + status.toString() 
				/*+ "' order by r.time desc"*/;
		
		return this.getHibernateTemplate().find(hql);
	}

	@Override
	public PushRecord savePushRecord(PushRecord record) {
		this.getHibernateTemplate().saveOrUpdate(record);
		return record;
	}

	@Override
	public PushRecord updatePushRecord(PushRecord record) {
		this.getHibernateTemplate().update(record);
		return record;
	}

	@Override
	public void removePushRecord(Long recordId) {
		PushRecord pr = this.getPushRecord(recordId);
		if(pr == null) return;
		this.getHibernateTemplate().delete(pr);
	}
	
	public void removePushRecord(PushRecord pr) {
		this.getHibernateTemplate().delete(pr);
	}

	@Override
	public void removeAllPushRecords(User user, Message message, PushStatus status) {
		List<PushRecord> list = this.getAllPushRecords(user, message, status);
		if(list == null) return;
		for(PushRecord pr : list) {
			this.removePushRecord(pr);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PushRecord> getAllPushRecords() {
		return this.getHibernateTemplate().find("from PushRecord p order by p.updateDate desc");
	}


}
