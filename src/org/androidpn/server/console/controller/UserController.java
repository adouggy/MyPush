/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.console.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.console.vo.UserVO;
import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.model.OnlineRecord;
import org.androidpn.server.model.User;
import org.androidpn.server.service.OnlineRecordService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.presence.PresenceManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * A controller class to process the user related requests.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class UserController extends MultiActionController {

	private UserService userService;
	private OnlineRecordService onlineService;

	public UserController() {
		userService = ServiceLocator.getUserService();
		onlineService = ServiceLocator.getOnlineRecordService();
	}

	public ModelAndView list(
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// String username = "b10f7cdc904a49939006839844b7edf4";
		// User user = userService.getUserByUsername(username);
		// OnlineRecord r = new OnlineRecord(user,
		// OnlineRecord.Action.login.toString(), new Date());
		//
		// onlineService.saveOnlineRecord(r);
		//
		ModelAndView mav = new ModelAndView();
		mav.setViewName("user/list");
		return mav;
	}

	public ModelAndView getUserList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println( ">>> get user list table." );

		String pageIndexStr = request.getParameter("pageIndex");
		String orderColumnStr = request.getParameter("orderColumn");
		String orderTypeStr = request.getParameter("orderType");
		
		int pageIndex = pageIndexStr == null? 1 : Integer.parseInt( pageIndexStr );
		int pageSize = 10;
		int userCount =  userService.getUserCount();
		
		int pageCount = userCount/pageSize + ((userCount%pageSize)==0?0:1); // make it to real world number
		int start = (pageIndex-1) * pageSize;
		
		System.out.println();
		System.out.println( String.format( ">>>>>Current Page:%d, Current user Index:%d, Page count:%d, total User:%d", pageIndex, start, pageCount, userCount ) );
		System.out.println();
		
		String orderColumn = "";
		if(orderColumnStr == null || orderColumnStr.equals("") || orderColumnStr.equals("online")/* || 不存在*/) {
			orderColumn = "createdDate";
		} else {
			orderColumn = orderColumnStr;
		}
		
		final Order orderType;
		if(orderTypeStr == null || orderTypeStr.equals("")) {
			orderType = Order.desc;
		} else {
			orderType = Order.valueOf(orderTypeStr);
		}	

		PresenceManager presenceManager = new PresenceManager();
		
		List<User> userList = userService.getUsers(orderColumn, orderType, start, pageSize);
		List<UserVO> voList = new ArrayList<UserVO>();
		for (User user : userList) {
			UserVO voUser = new UserVO(user.getUsername(), user.getName(), user.getEmail(), user.getCreatedDate(),(int)user.getId().longValue(), user.getPartner(), user.getBirthday() );
			if (presenceManager.isAvailable(user)) {
				user.setOnline(true);
				voUser.setOnline(true);
			} else {
				user.setOnline(false);
				voUser.setOnline(false);
			}
			
			//set the online time
			voUser.setOnline_percent_lastDay(this.getLastDayOnlinePercent(user));
			voUser.setOnline_percent_lastWeek(this.getlastWeekOnlinePercent(user));
			voUser.setOnline_percent_lastMonth(this.getlastMonthOnlinePercent(user));
			voUser.setOnline_percent_lastHour(this.getLastHourOnlinePercent(user));
			
			voList.add(voUser);
		}
		
		if("online".equals(orderColumnStr)) {
			orderColumn = "online";
			List<UserVO> onlineList = new ArrayList<UserVO>();
			List<UserVO> offlineList = new ArrayList<UserVO>();			
			for(UserVO user : voList) {
				if(user.isOnline()) 
					onlineList.add(user);
				else
					offlineList.add(user);
			}
			voList.clear();
			if(orderType == Order.desc) {
				voList.addAll(onlineList);
				voList.addAll(offlineList);
			} else {
				voList.addAll(offlineList);
				voList.addAll(onlineList);
			}

		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("userList", voList);
		mav.addObject("pageIndex", pageIndex);
		mav.addObject("pageCount", pageCount);
		
		mav.addObject("orderColumn", orderColumn);
		mav.addObject("orderType", orderType.toString());
		
		mav.setViewName("user/table");
		return mav;
	}

	public double getLastHourOnlinePercent(
			User user) {
		Calendar cal = Calendar.getInstance();
		Date endTime = cal.getTime();

		cal.add(Calendar.HOUR_OF_DAY,
				-1);
		Date startTime = cal.getTime();

		return this.getOnlinePercent(user,
				startTime,
				endTime);
	}

	public double getLastDayOnlinePercent(
			User user) {
		Calendar cal = Calendar.getInstance();
		Date endTime = cal.getTime();

		cal.add(Calendar.DAY_OF_YEAR,
				-1);
		Date startTime = cal.getTime();

		return this.getOnlinePercent(user,
				startTime,
				endTime);
	}

	public double getlastWeekOnlinePercent(
			User user) {
		Calendar cal = Calendar.getInstance();
		Date endTime = cal.getTime();

		cal.add(Calendar.DAY_OF_YEAR,
				-7);
		Date startTime = cal.getTime();

		return this.getOnlinePercent(user,
				startTime,
				endTime);
	}

	public double getlastMonthOnlinePercent(
			User user) {
		Calendar cal = Calendar.getInstance();
		Date endTime = cal.getTime();

		cal.add(Calendar.DAY_OF_YEAR,
				-30);
		Date startTime = cal.getTime();

		return this.getOnlinePercent(user,
				startTime,
				endTime);
	}

	// 得到在线百分比
	public double getOnlinePercent(
			User user,
			Date startTime,
			Date endTime) {
		List<OnlineRecord> list = this.getOnlineList(user,
				startTime,
				endTime);

		Date logout = null;
		Date login = null;
		Long onlineTime = 0l;
		for (int j = 0; j < list.size(); j++) {
			OnlineRecord r = list.get(j);
			if (OnlineRecord.Action.logout.equals(r.getAction())) {
				logout = r.getTime();
			} else if (OnlineRecord.Action.login.equals(r.getAction()) && logout != null) {
				login = r.getTime();
				onlineTime += logout.getTime() - login.getTime();
				logout = null;
			}
		}

		return 1.0d * onlineTime / (endTime.getTime() - startTime.getTime());
	}

	// get online list
	public List<OnlineRecord> getOnlineList(
			User user,
			Date startTime,
			Date endTime) {
		List<OnlineRecord> list = onlineService.getOnlineRecords(user,
				startTime,
				endTime,
				"time",
				Order.desc);

		// 如果结果为0，则要看一下在这之前和之后是否有login和logout的记录，如果有则表示这段时间一直在线着
		if (list != null && list.size() == 0) {
			List<OnlineRecord> before = onlineService.getOnlineRecords(user,
					null,
					startTime,
					"time",
					Order.desc);
			List<OnlineRecord> after = onlineService.getOnlineRecords(user,
					endTime,
					null,
					"time",
					Order.asc);
			// 如果之前是login，则表示此时段在线，给列表暂时增加一个startTime的login记录
			if (before.size() > 0 && before.get(0).getAction().equals(OnlineRecord.Action.login)) {
				OnlineRecord record = new OnlineRecord(user, OnlineRecord.Action.login, startTime);
				list.add(list.size(),
						record);
			}
			// 如果之后是logout，则表示此时段在线，给列表暂时增加一个endTime的logout记录
			if (after.size() > 0 && after.get(0).getAction().equals(OnlineRecord.Action.logout)) {
				OnlineRecord record = new OnlineRecord(user, OnlineRecord.Action.logout, endTime);
				list.add(0,
						record);
			}
		}

		if (list == null || list.size() <= 0) {
			return new ArrayList<OnlineRecord>();
		}

		// 如果第一个是login，则认为用户在endTime时未登出，还在登录中，给列表暂加一个endTime时间的登出记录
		if (OnlineRecord.Action.login.equals(list.get(0).getAction())) {
			OnlineRecord record = new OnlineRecord(user, OnlineRecord.Action.logout, endTime);
			list.add(0,
					record);
		}
		// 如果最后一个是logout，则认为用户在startTime时已登入，在登录中，给列表暂加一个startTime时间的登入记录
		if (OnlineRecord.Action.logout.equals(list.get(list.size() - 1).getAction())) {
			OnlineRecord record = new OnlineRecord(user, OnlineRecord.Action.login, startTime);
			list.add(list.size(),
					record);
		}
		return list;
	}

}
