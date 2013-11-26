package org.androidpn.server.service;

import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.model.OnlineUserCount;

public interface OnlineUserCountService {
	

	public OnlineUserCount saveRecord(OnlineUserCount record);
	
	public OnlineUserCount getRecord(Date time);

	public List<OnlineUserCount> getAllRecords(Date start, Date end, String orderColumn, Order order);
	
}
