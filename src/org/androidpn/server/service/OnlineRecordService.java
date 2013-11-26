package org.androidpn.server.service;

import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.model.OnlineRecord;
import org.androidpn.server.model.User;

public interface OnlineRecordService {
	
	public OnlineRecord getOnlineRecord(Long recordId);
	
	public List<OnlineRecord> getOnlineRecords(User user, String action);
	
	public List<OnlineRecord> getOnlineRecords(User user, Date startTime, Date endTime, String orderColumn, Order order);
	
	public List<OnlineRecord> getOnlineRecordsByUser(User user);
	
	public List<OnlineRecord> getOnlineRecordsByAction(String action);
	
	public OnlineRecord saveOnlineRecord(OnlineRecord record);
	
	public void removeOnlineRecord(OnlineRecord record);
	
	public void removeOnlineRecordByUser(User user);
	
	public int getLoginCount(User user);
	
	public int getLoginCount(User user, Date startTime, Date endTime);
	
	public Date getLastLoginTime(User user);
	
	public Long getOnlineTotalTime(User user);
	
}
