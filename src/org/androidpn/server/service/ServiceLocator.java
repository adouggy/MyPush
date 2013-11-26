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
package org.androidpn.server.service;

import org.androidpn.server.xmpp.XmppServer;

/**
 * This is a helper class to look up service objects.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class ServiceLocator {

	public static String USER_SERVICE = "userService";
	public static String ONLINE_RECORD_SERVICE = "onlineRecordService";
	public static String MESSAGE_SERVICE = "messageService";
	public static String PUSH_RECORD_SERVICE = "pushRecordService";
	public static String ONLINE_USER_COUNT_SERVICE = "onlineUserCountService";

	/**
	 * Generic method to obtain a service object for a given name.
	 * 
	 * @param name
	 *            the service bean name
	 * @return
	 */
	public static Object getService(String name) {
		return XmppServer.getInstance().getBean(name);
	}

	/**
	 * Obtains the user service.
	 * 
	 * @return the user service
	 */
	public static UserService getUserService() {
		return (UserService) XmppServer.getInstance().getBean(USER_SERVICE);
	}

	public static OnlineRecordService getOnlineRecordService() {
		return (OnlineRecordService) XmppServer.getInstance().getBean(ONLINE_RECORD_SERVICE);
	}

	public static MessageService getMessageService() {
		return (MessageService) XmppServer.getInstance().getBean(MESSAGE_SERVICE);
	}

	public static PushRecordService getPushRecordService() {
		return (PushRecordService) XmppServer.getInstance().getBean(PUSH_RECORD_SERVICE);
	}

	public static OnlineUserCountService getOnlineUserCountService() {
		return (OnlineUserCountService) XmppServer.getInstance().getBean(ONLINE_USER_COUNT_SERVICE);
	}
}
