package org.androidpn.server.service.impl;

import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.OnlineRecordDao;
import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.model.OnlineRecord;
import org.androidpn.server.model.User;
import org.androidpn.server.service.OnlineRecordService;

public class OnlineRecordServiceImpl implements OnlineRecordService{

	private OnlineRecordDao onlineRecordDao;
	 
	public void setOnlineRecordDao(OnlineRecordDao onlineRecordDao) {
		this.onlineRecordDao = onlineRecordDao;
	}

	@Override
	public OnlineRecord getOnlineRecord(Long recordId) {
		return onlineRecordDao.getOnlineRecord(recordId);
	}

	@Override
	public List<OnlineRecord> getOnlineRecordsByUser(User user) {
		return onlineRecordDao.getOnlineRecordsByUser(user);
	}

	@Override
	public List<OnlineRecord> getOnlineRecordsByAction(String action) {
		return onlineRecordDao.getOnlineRecordsByAction(action);
	}

	@Override
	public List<OnlineRecord> getOnlineRecords(User user, String action) {
		return onlineRecordDao.getOnlineRecords(user, action);
	}

	@Override
	public List<OnlineRecord> getOnlineRecords(User user, Date startTime, Date endTime, String orderColumn, Order order) {
		return onlineRecordDao.getOnlineRecords(user, startTime, endTime, orderColumn, order);
	}
	
	@Override
	public OnlineRecord saveOnlineRecord(OnlineRecord record) {
		return onlineRecordDao.saveOnlineRecord(record);
	}

	@Override
	public void removeOnlineRecord(OnlineRecord record) {
		onlineRecordDao.removeOnlineRecord(record);
	}

	@Override
	public void removeOnlineRecordByUser(User user) {
		onlineRecordDao.removeOnlineRecordByUser(user);
	}

	@Override
	public int getLoginCount(User user) {
		return onlineRecordDao.getLoginCount(user);
	}

	@Override
	public Long getOnlineTotalTime(User user) {
		return onlineRecordDao.getOnlineTotalTime(user);
	}

	@Override
	public int getLoginCount(User user, Date startTime, Date endTime) {
		return onlineRecordDao.getLoginCount(user, startTime, endTime);
	}

	@Override
	public Date getLastLoginTime(User user) {
		return onlineRecordDao.getLastLoginTime(user);
	}

}
