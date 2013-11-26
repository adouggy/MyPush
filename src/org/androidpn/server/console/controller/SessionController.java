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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.console.vo.SessionVO;
import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.xmpp.packet.Presence;

/** 
 * A controller class to process the session related requests.  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class SessionController extends MultiActionController {

    //private UserService userService;

    public SessionController() {
        //userService = ServiceLocator.getUserService();
    }

    public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        System.out.println("session get...");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("session/list");
        return mav;
    }
    
    public ModelAndView getSessionList(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        System.out.println("session get list...");
        
        ClientSession[] sessions = new ClientSession[0];
        sessions = SessionManager.getInstance().getSessions().toArray(sessions);

		String pageIndexStr = request.getParameter("pageIndex");
		String orderColumnStr = request.getParameter("orderColumn");
		String orderTypeStr = request.getParameter("orderType");
		
		int pageIndex = pageIndexStr == null? 1 : Integer.parseInt( pageIndexStr );
		int pageSize = 10;
		int totalCount =  sessions.length;
		
		int pageCount = totalCount/pageSize + ((totalCount%pageSize)==0?0:1); // make it to real world number
		int start = (pageIndex-1) * pageSize + 1;
        
        List<SessionVO> voList = new ArrayList<SessionVO>();
        for(int i=0; i<sessions.length; i++) {
        	ClientSession sess = sessions[i];
            SessionVO vo = new SessionVO();
            vo.setUsername(sess.getUsername());
            vo.setResource(sess.getAddress().getResource());
            // Status
            if (sess.getStatus() == Session.STATUS_CONNECTED) {
                vo.setStatus("CONNECTED");
            } else if (sess.getStatus() == Session.STATUS_AUTHENTICATED) {
                vo.setStatus("AUTHENTICATED");
            } else if (sess.getStatus() == Session.STATUS_CLOSED) {
                vo.setStatus("CLOSED");
            } else {
                vo.setStatus("UNKNOWN");
            }
            // Presence
            if (!sess.getPresence().isAvailable()) {
                vo.setPresence("Offline");
            } else {
                Presence.Show show = sess.getPresence().getShow();
                if (show == null) {
                    vo.setPresence("Online");
                } else if (show == Presence.Show.away) {
                    vo.setPresence("Away");
                } else if (show == Presence.Show.chat) {
                    vo.setPresence("Chat");
                } else if (show == Presence.Show.dnd) {
                    vo.setPresence("Do Not Disturb");
                } else if (show == Presence.Show.xa) {
                    vo.setPresence("eXtended Away");
                } else {
                    vo.setPresence("Unknown");
                }
            }
            vo.setClientIP(sess.getHostAddress());
            vo.setCreatedDate(sess.getCreationDate());
            voList.add(vo);
        }

		String orderColumn = "";
		if(orderColumnStr == null || orderColumnStr.equals("")/* || 不存在*/) {
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
        
        this.sortData(voList, orderColumn, orderType);
        
        List<SessionVO> listInThePage = new ArrayList<SessionVO>();
        for(int i=start-1; i<Math.min(start+pageSize-1, totalCount); i++) {
        	listInThePage.add(voList.get(i));
        }
        
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionList", listInThePage);
		mav.addObject("pageIndex", pageIndex);
		mav.addObject("pageCount", pageCount);
		
		mav.addObject("totalCount", totalCount);
		mav.addObject("orderColumn", orderColumn);
		mav.addObject("orderType", orderType.toString());
        mav.setViewName("session/table");
        return mav;
    }

	private void sortData(List<SessionVO> data, final String item, final Order order) {
		Collections.sort(data, new Comparator<SessionVO>(){
			@Override
			public int compare(SessionVO lhs, SessionVO rhs) {
				int flag = 0;
				if("createdDate".equals(item)) {
					flag = lhs.getCreatedDate().compareTo(rhs.getCreatedDate());
				} else if("clientIp".equals(item)) {
					flag = lhs.getClientIP().compareTo(rhs.getClientIP());
				} else if("resource".equals(item)) {
					flag = lhs.getResource().compareTo(rhs.getResource());
				} else if("username".equals(item)) {
					flag = lhs.getUsername().compareTo(rhs.getUsername());
				}   
				if(order == Order.desc) {
					flag = -1 * flag;
				}
				return flag;
			}
			
		});
	}
    
}
