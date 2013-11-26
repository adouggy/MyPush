package org.androidpn.server.service.impl;

import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.MessageDao;
import org.androidpn.server.model.Message;
import org.androidpn.server.service.MessageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessageServiceImpl implements MessageService {

    protected final Log log = LogFactory.getLog(getClass());

    private MessageDao messageDao;
    
	@Override
	public Message getMessage(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message saveMessage(Message message) {
		Message msg = messageDao.saveMessage(message);
		return msg;
	}

	@Override
	public void removeMessage(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Message> getMessagesByTime(Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	public MessageDao getmessageDao() {
		return messageDao;
	}

	public void setmessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	@Override
	public List<Message> getMessages() {
		return this.messageDao.getMessages();
	}
}
