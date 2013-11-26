package org.androidpn.server.console.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.synergyinfosys.xmpp.bean.PushStatus;

import org.androidpn.server.model.Message;
import org.androidpn.server.model.User;
import org.androidpn.server.service.MessageService;
import org.androidpn.server.service.PushRecordService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class NotificationController extends MultiActionController {

	private NotificationManager notificationManager;
	
	private PushRecordService pushRecordService;
	private MessageService messageService;
	private UserService userService;

	public NotificationController() {
		notificationManager = new NotificationManager();
		
		userService = ServiceLocator.getUserService();
		messageService = ServiceLocator.getMessageService();
		pushRecordService = ServiceLocator.getPushRecordService();
	}

	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("notification/form");
		return mav;
	}

	public ModelAndView send(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String broadcast = ServletRequestUtils.getStringParameter(request, "broadcast", "Y");
		String username = ServletRequestUtils.getStringParameter(request, "username");
		String title = ServletRequestUtils.getStringParameter(request, "title");
		String message = ServletRequestUtils.getStringParameter(request, "message");
		String uri = ServletRequestUtils.getStringParameter(request, "uri");

		String apiKey = Config.getString("apiKey", "");
		logger.debug("apiKey=" + apiKey);
		
		List<User> userList = null;
		
		if (broadcast.equalsIgnoreCase("Y")) {
			notificationManager.sendBroadcast(apiKey, title, message, uri);
			userList = userService.getUsers();
		} else {
			notificationManager.sendNotifcationToUser(apiKey, username, title, message, uri);
			User u = userService.getUserByUsername(username);
			userList = new ArrayList<User>();
			userList.add(u);
		}
		
		logger.info("One message incoming..");
		
		logger.info("storing.");
		Message msg = new Message();
		msg.setMessage(message);
		msg = messageService.saveMessage(msg);
		logger.info(msg.getId() + " is the saved ID");
		for( User u : userList ){
			pushRecordService.addPushRecord(u.getId(), msg.getId(), PushStatus.Sent);
		}

		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:notification.do");
		return mav;
	}

}
