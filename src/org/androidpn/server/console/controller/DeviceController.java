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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * A controller class to process the device(MDM) related requests.
 * 
 * @author ade
 */
public class DeviceController extends MultiActionController {

	private UserService userService;

	public DeviceController() {
		userService = ServiceLocator.getUserService();
	}

	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println( ">>> listing users." );
		List<User> userList = userService.getUsers();
		
		String selectedUsername = request.getParameter("username");
		
		User user = null;
		
		if( selectedUsername != null )
			user = userService.getUserByUsername(selectedUsername);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("userList", userList);
		mav.addObject("username", selectedUsername);
		if( user != null ){
			String appList = user.getAppList();
			if( appList != null ){
				//mav.addObject("appList", appList.replaceAll(";", "<br/>"));
				String[] apps = appList.split(";");
				List<String> appArr = new ArrayList<String>(apps.length);
				for( String s : apps ){
					appArr.add( s );
				}
				mav.addObject("appList", appArr);
			}
			
			mav.addObject("selectedUser", user);
		}
		mav.setViewName("device/info");
		return mav;
	}

}
