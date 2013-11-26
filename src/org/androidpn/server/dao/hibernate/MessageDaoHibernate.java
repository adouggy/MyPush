package org.androidpn.server.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.MessageDao;
import org.androidpn.server.model.Message;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MessageDaoHibernate extends HibernateDaoSupport implements MessageDao {

	@Override
	public Message getMessage(Long id) throws Exception {
		Message msg = (Message) this.getHibernateTemplate().get(Message.class, id);
		if( msg == null ){
			throw new Exception("Message which id==" + id + " can't find!");
		}
		return msg;
	}

	@Override
	public Message saveMessage(Message message) {
		getHibernateTemplate().saveOrUpdate(message);
		getHibernateTemplate().flush();
		return message;
	}

	@Override
	public void removeMessage(Long id) throws DataAccessException, Exception {
		getHibernateTemplate().delete(getMessage(id));
		
	}

	@Override
	public List<Message> getMessagesByTime(Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getMessages() {
		return getHibernateTemplate().find("from Message m order by m.createdDate desc");
	}

}
