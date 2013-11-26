package org.androidpn.server.service.impl;

import java.util.List;

import net.synergyinfosys.xmpp.bean.PushStatus;

import org.androidpn.server.dao.MessageDao;
import org.androidpn.server.dao.PushRecordDao;
import org.androidpn.server.dao.UserDao;
import org.androidpn.server.model.Message;
import org.androidpn.server.model.PushRecord;
import org.androidpn.server.model.User;
import org.androidpn.server.service.PushRecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PushRecordServiceImpl implements PushRecordService {

    protected final Log log = LogFactory.getLog(getClass());
 
    private PushRecordDao pushRecordDao;
    
    private UserDao userDao;
    
    private MessageDao messageDao;
    
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public PushRecordDao getPushRecordDao() {
		return pushRecordDao;
	}

	public void setPushRecordDao(PushRecordDao pushRecordDao) {
		this.pushRecordDao = pushRecordDao;
	}

	@Override
	public PushRecord getPushRecord(Long recordId) {
		return pushRecordDao.getPushRecord(recordId);
	}

	@Override
	public List<PushRecord> getAllPushRecords(User user, Message message, PushStatus status) {
		return pushRecordDao.getAllPushRecords(user, message, status);
	}

	@Override
	public PushRecord updatePushRecord(PushRecord record) {
		return pushRecordDao.updatePushRecord(record);
	}

	@Override
	public void removePushRecord(Long recordId) {
		pushRecordDao.removePushRecord(recordId);
	}

	@Override
	public void removeAllPushRecords(User user, Message message, PushStatus status) {
		pushRecordDao.removeAllPushRecords(user, message, status);
		
	}

	/**
	 * both user and message should exists! or exception throws, no push record will create.
	 * 
	 * @throws Exception 
	 */
	@Override
	public PushRecord addPushRecord( long userId, long messageId, PushStatus status) throws Exception {
		
		PushRecord pushRecord = new PushRecord();
		
		pushRecord.setUser(userDao.getUser(userId));
		pushRecord.setMessage(messageDao.getMessage(messageId));
		pushRecord.setStatus( status );
		
		PushRecord savedRecord = pushRecordDao.savePushRecord(pushRecord);
		
		return savedRecord;
	}

	@Override
	public List<PushRecord> getAllPushRecords() {
		return this.pushRecordDao.getAllPushRecords();
	}


}
