package org.androidpn.server.console.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.User;
import org.androidpn.server.service.MessageService;
import org.androidpn.server.service.PushRecordService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.presence.PresenceManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/** 
 * a mvc for manage push service
 *
 * @author ade
 */
public class ManageController extends MultiActionController {

	private UserService userService;
	private MessageService messageService;
	private PushRecordService pushrecordService;
	private PresenceManager presenceManager;
	
    public ManageController(){
    	userService = ServiceLocator.getUserService();
    	messageService = ServiceLocator.getMessageService();
    	pushrecordService = ServiceLocator.getPushRecordService();
    	presenceManager = new PresenceManager();
    }

    public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	int onlineCount = 0;
		List<User> userList = userService.getUsers();
		for (User user : userList) {
			if (presenceManager.isAvailable(user)) {
				onlineCount ++;
			}
		}
		
		int msgCount = messageService.getMessages().size();
		
		int sentCount = pushrecordService.getAllPushRecords().size();
		
		//set model / view
		ModelAndView mav = new ModelAndView();
		mav.addObject("onlineCount", onlineCount);
		mav.addObject("totalCount", userList.size());
		mav.addObject("msgCount", msgCount);
		mav.addObject("sentCount", sentCount);
		mav.setViewName("management/function");			
        return mav;
    }

}
