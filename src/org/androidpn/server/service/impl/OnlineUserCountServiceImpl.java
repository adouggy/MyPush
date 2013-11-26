package org.androidpn.server.service.impl;

import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.OnlineUserCountDao;
import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.model.OnlineUserCount;
import org.androidpn.server.service.OnlineUserCountService;

public class OnlineUserCountServiceImpl implements OnlineUserCountService{

	private OnlineUserCountDao onlineUserCountDao;


	public OnlineUserCountDao getOnlineUserCountDao() {
		return onlineUserCountDao;
	}

	public void setOnlineUserCountDao(OnlineUserCountDao onlineUserCountDao) {
		this.onlineUserCountDao = onlineUserCountDao;
	}

	@Override
	public OnlineUserCount saveRecord(OnlineUserCount record) {
		return onlineUserCountDao.saveRecord(record);
	}

	@Override
	public OnlineUserCount getRecord(Date time) {
		return onlineUserCountDao.getRecord(time);
	}

	@Override
	public List<OnlineUserCount> getAllRecords(Date start, Date end, String orderColumn, Order order) {
		return onlineUserCountDao.getAllRecords(start, end, orderColumn, order);
	}


}
