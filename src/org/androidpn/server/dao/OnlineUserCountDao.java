package org.androidpn.server.dao;

import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.model.OnlineUserCount;

public interface OnlineUserCountDao {
	
//	public UserMessage getPushRecord(Long recordId);
//	
//	public List<UserMessage> getAllPushRecords(User user, Message message, PushStatus status);

	public OnlineUserCount saveRecord(OnlineUserCount record);
	
	public OnlineUserCount getRecord(Date time);

	public List<OnlineUserCount> getAllRecords(Date start, Date end, String ordercolumn, Order order);
	
//	public void removePushRecord(Long recordId);
//	
//	public void removeAllPushRecords(User user, Message message, PushStatus status);

}
