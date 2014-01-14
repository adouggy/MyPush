package me.promenade.xmpp.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import me.promenade.xmpp.bean.PushMessage;
import me.promenade.xmpp.bean.PushStatus;
import me.promenade.xmpp.bean.PushTask;
import me.promenade.xmpp.service.PushStatusMap;
import me.promenade.xmpp.service.PushThreadPool;

import org.androidpn.server.console.vo.SessionVO;
import org.androidpn.server.dao.UserDao;
import org.androidpn.server.dao.UserPhotoDao;
import org.androidpn.server.model.Message;
import org.androidpn.server.model.User;
import org.androidpn.server.model.UserPhoto;
import org.androidpn.server.service.MessageService;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.spring.Autowire;

@Autowire
@Path("/xmpp")
public class XmppResource {

	private static final Log log = LogFactory.getLog(XmppResource.class);

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	private UserDao userDao;
	private UserPhotoDao userPhotoDao;

	private MessageService messageService;

	public void setUserDao(
			UserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserPhotoDao(
			UserPhotoDao userPhotoDao) {
		this.userPhotoDao = userPhotoDao;
	}

	public void setMessageService(
			MessageService messageService) {
		this.messageService = messageService;
	}

	public XmppResource() {
	}

	/**
	 * 该函数用于调用推送服务器的推送功能 重新定义了一个简单的
	 * 
	 * @param input
	 *            JSON: {devId:"xxx", data:"base64here"}
	 * @return JSON: : {status:"xxx", msg:"xxx", id:"uuid_here"}
	 */
	@Path("/push")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject push(
			JSONObject param) {
		log.debug("/xmpp/push..");

		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("status",
					"error");
			responseJson.put("msg",
					"wrong input");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (param == null)
			return responseJson;
		log.debug(param.toString());

		String devId, data;
		try {
			devId = param.getString("devId");
			data = param.getString("data");
		} catch (JSONException e) {
			// e.printStackTrace();
			/**
			 * 这里已经捕获了异常，不打印了
			 */
			return responseJson;
		}

		if (devId == null || data == null || devId.length() == 0 || data.length() == 0) {
			return responseJson;
		}

		Message message = new Message();
		message.setMessage(data);

		String uuid = UUID.randomUUID().toString().replaceAll("-",
				"");
		PushMessage msg = new PushMessage.Builder().id(uuid).devId(devId).data(data).build();
		PushTask task = new PushTask(uuid, msg);
		String pushResult = PushThreadPool.INSTANCE.push(task);
		if (pushResult == null) {
			try {
				responseJson.put("status",
						"error");
				responseJson.put("msg",
						"Thread pool is full");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return responseJson;
		}

		// store msg
		log.info("storing.");
		org.androidpn.server.model.Message m = new Message();
		m.setMessage(data);
		m = messageService.saveMessage(m);
		log.info(msg.getId() + " is the saved ID");

		try {
			responseJson.put("id",
					uuid);
			responseJson.put("status",
					"ok");
			responseJson.put("msg",
					"finished");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return responseJson;
	}

	/**
	 * call back
	 * 
	 * @param HTTP
	 *            header: Cookie, with authorized
	 * @param JSON
	 *            , format: {id:"xxx", type:"xxx", data:"xxxx"}
	 * @category type:{ confirm}
	 * @category data:{ ok, error}
	 * @return JSON, format: {status:"xxx", msg:"xxx"}
	 */
	@Path("/callback")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject callback(
			JSONObject param) {
		log.debug("/xmpp/callback");
		JSONObject resJson = new JSONObject();
		try {
			resJson.put("status",
					"Error");
			resJson.put("msg",
					"Wrong input");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (param == null) {
			return resJson;
		}

		try {
			String id = param.getString("id");
			String type = param.getString("type");
			String data = param.getString("data");

			if (id == null || type == null || data == null) {
				return resJson;
			}

			boolean isOk = false;
			if (type.toLowerCase().compareTo("confirm") == 0) {
				if (data.toLowerCase().compareTo("ok") == 0) {
					isOk = PushStatusMap.INSTANCE.changeStatus(id,
							PushStatus.FinishedOK);
				} else if (data.toLowerCase().compareTo("error") == 0) {
					isOk = PushStatusMap.INSTANCE.changeStatus(id,
							PushStatus.FinishedError);
				}
			}
			if (!isOk) {
				resJson.put("status",
						"Error");
				resJson.put("msg",
						"No such ID");
				return resJson;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			resJson.put("status",
					"ok");
			resJson.put("msg",
					"finsished");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return resJson;
	}

	/**
	 * register the user into database.
	 * 
	 * @param input
	 *            JSON: {username:"xxx", password:"xxxx", email:"xxxx",
	 *            name:"xxxx"}
	 * @param output
	 *            JSON: {status:"ok/error/duplicated", msg:"message here",
	 *            id:"user id"}
	 * @return
	 */
	@Path("/register")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject register(
			JSONObject param) {
		log.info("/xmpp/register");
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("status",
					"error");
			responseJson.put("msg",
					"wrong input");
			responseJson.put("id",
					"-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (param == null) {
			return responseJson;
		}

		log.info("Input JSON:" + param.toString());

		String username, password, email, name, genderStr;
		long birthday;
		int partner;

		try {
			username = param.getString("username");
			password = param.getString("password");
			email = param.getString("email");
			name = param.getString("name");
			birthday = param.getLong("birthday");
			genderStr = param.getString("gender");

			if (!param.isNull("partner")) {
				partner = param.getInt("partner");
			} else {
				partner = -1;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return responseJson;
		}

		if (username == null || username.length() == 0 || password == null || password.length() == 0) {
			return responseJson;
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setName(name);
		user.setBirthday(birthday);
		user.setGender((genderStr == null || genderStr.compareTo("1") != 0) ? false : true);
		user.setPartner(partner);

		try {
			responseJson.put("status",
					"error");
			responseJson.put("id",
					"-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// if registered, do something else
		log.info("checking if " + username + " duplicated..");
		User u = userDao.getUserByUsername(username);
		if (u != null) {
			try {
				responseJson.put("status",
						"duplicate");
				responseJson.put("id",
						u.getId());
				return responseJson;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		try {
			user = userDao.saveUser(user);
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				responseJson.put("msg",
						"user insert error:" + e1.getMessage());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return responseJson;
		}

		log.info("New created user:" + user.toString());
		try {
			responseJson.put("status",
					"ok");
			responseJson.put("msg",
					"user created");
			responseJson.put("id",
					user.getId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	/**
	 * confirm the user .
	 * 
	 * @param input
	 *            JSON: {username:"xxx", password:"xxxx"}
	 * @param output
	 *            JSON: {status:"ok/error", msg:"message here", id:"user id"}
	 * @return
	 */
	@Path("/confirm")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject confirm(
			JSONObject param) {
		log.info("/xmpp/confirm");
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("status",
					"error");
			responseJson.put("msg",
					"wrong input");
			responseJson.put("id",
					"-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (param == null) {
			return responseJson;
		}

		log.info("Input JSON:" + param.toString());

		String username, password;
		try {
			username = param.getString("username");
			password = param.getString("password");
		} catch (JSONException e) {
			e.printStackTrace();
			return responseJson;
		}

		if (username == null || username.length() == 0 || password == null || password.length() == 0) {
			return responseJson;
		}

		try {
			responseJson.put("status",
					"error");
			responseJson.put("msg",
					"no such user");
			responseJson.put("id",
					"-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		User user = userDao.getUserByUsername(username);
		if (user == null) {
			return responseJson;
		}

		try {
			responseJson.put("status",
					"error");
			responseJson.put("msg",
					"wrong password");
			responseJson.put("id",
					"-1");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (!password.equals(user.getPassword())) {
			return responseJson;
		}

		log.info("user confirm ok.");
		System.out.println("user comfirm ok.");
		try {
			responseJson.put("status",
					"ok");
			responseJson.put("msg",
					"user comfirm ok");
			responseJson.put("id",
					user.getId());
			responseJson.put("email",
					user.getEmail());
			responseJson.put("partner",
					user.getPartner());
			responseJson.put("gender",
					user.isGender());
			responseJson.put("birthday",
					user.getBirthday());
			responseJson.put("name",
					user.getName());
			responseJson.put("username",
					user.getUsername());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("response: " + responseJson.toString());
		return responseJson;
	}

	/**
	 * delete user
	 * 
	 * @return
	 */
	@Path("/user/{userid}")
	@DELETE
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject deleteUserById(
			@PathParam("userid") String id) {
		log.info("/user/" + id + " [DELETE] invoke..");

		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("status",
					"error");
			responseJson.put("msg",
					"wrong input");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			userDao.removeUser(Long.parseLong(id));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return responseJson;
		} catch (UserNotFoundException e1) {
			try {
				responseJson.put("msg",
						"User not found");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return responseJson;
		}

		try {
			responseJson.put("status",
					"ok");
			responseJson.put("msg",
					"removed");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return responseJson;
	}

	/**
	 * get user
	 * 
	 * @return
	 * @throws JSONException
	 */
	@Path("/user/{userid}")
	@GET
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUser(
			@PathParam("userid") String id) throws JSONException {
		log.info("/user/" + id + " [DELETE] invoke..");

		JSONObject responseJson = new JSONObject();
		responseJson.put("status",
				"error");
		responseJson.put("msg",
				"wrong input");

		User u = null;
		try {
			u = userDao.getUser(Long.parseLong(id));
		} catch (NumberFormatException e) {
			return responseJson;
		} catch (UserNotFoundException e1) {
			try {
				responseJson.put("msg",
						"User not found");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return responseJson;
		}

		ClientSession[] sessions = new ClientSession[0];
		sessions = SessionManager.getInstance().getSessions().toArray(sessions);

		List<SessionVO> voList = new ArrayList<SessionVO>();
		String presence = "N/A";
		for (ClientSession sess : sessions) {
			String sessUserName = null;
			try {
				sessUserName = sess.getUsername();
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			}
			if (sessUserName != null && u.getUsername().compareTo(sessUserName) == 0) {
				if (sess.getPresence().isAvailable()) {
					presence = "online";
				} else {
					presence = "offline";
				}
				break;
			}
		}

		try {
			responseJson.put("status",
					"ok");
			String username = u.getUsername();
			String userId = String.valueOf(u.getId());
			responseJson.put("username",
					username);
			responseJson.put("userId",
					userId);
			responseJson.put("presence",
					presence);
			responseJson.put("email",
					u.getEmail());
			responseJson.put("birthday",
					u.getBirthday());
			responseJson.put("msg",
					"user found");
			responseJson.put("partner",
					u.getPartner());
			responseJson.put("gender",
					u.isGender());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return responseJson;
	}

	@Path("/PushStatus")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String pushStatus(
			@QueryParam("id") String id) {
		log.info("/xmpp/PushStatus?id=" + id);
		if (id != null) {
			PushStatus ps = PushStatusMap.INSTANCE.getStatus(id);
			if (ps == null) {
				return null;
			} else {
				return ps.toString();
			}
		} else {
			return PushStatusMap.INSTANCE.toString();
		}
	}

	@Path("/addPartner/{user1}/{user2}")
	@GET
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject addPartner(
			@PathParam("user1") String name1,
			@PathParam("user2") String name2) throws JSONException {
		log.info("try to add friend " + name1 + "<->" + name2);

		JSONObject responseJson = new JSONObject();
		responseJson.put("status",
				"error");
		responseJson.put("msg",
				"wrong input");

		User user1 = null, user2 = null;
		user1 = userDao.getUserByUsername(name1);
		user2 = userDao.getUserByUsername(name2);

		if (user1 == null) {
			responseJson.put("msg",
					"user1 not exists");
			return responseJson;
		}
		if (user2 == null) {
			responseJson.put("msg",
					"user2 not exists");
			return responseJson;
		}

		user1.setPartner((int) user2.getId().longValue());
		userDao.saveUser(user1);

		responseJson.put("status",
				"ok");
		responseJson.put("msg",
				"partner added");

		return responseJson;
	}

	@Path("/getPhoto/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getPhoto(
			@PathParam("userId") long userId) throws JSONException {
		log.info("try to get photo for " + userId);

		JSONObject responseJson = new JSONObject();
		responseJson.put("status",
				"error");
		responseJson.put("msg",
				"wrong input");

		User user1 = null;
		try {
			user1 = userDao.getUser(userId);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}

		if (user1 == null) {
			responseJson.put("msg",
					"user not exists");
			return responseJson;
		}

		UserPhoto up = userPhotoDao.getUserPhotoByUserId(user1.getId());
		if (up == null) {
			responseJson.put("msg",
					"no photo for user yet");
			return responseJson;
		}

		log.info("photo:" + up.getPhoto());

		responseJson.put("status",
				"ok");
		responseJson.put("msg",
				"found photo");
		responseJson.put("photo",
				up.getPhoto());

		return responseJson;

	}

	@Path("/addPhoto/{userId}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject addPhoto(
			@PathParam("userId") long userId,
			JSONObject param) throws JSONException {
		log.info("try to add photo for " + userId);

		JSONObject responseJson = new JSONObject();
		responseJson.put("status",
				"error");
		responseJson.put("msg",
				"wrong input");

		User user1 = null;
		try {
			user1 = userDao.getUser(userId);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}

		if (user1 == null) {
			responseJson.put("msg",
					"user not exists");
			return responseJson;
		}

		UserPhoto up = userPhotoDao.getUserPhotoByUserId(user1.getId());
		if (up == null) {
			up = new UserPhoto();
		}
		log.info("photo:" + param.toString());
		String base64 = param.getString("photo");

		up.setPhoto(base64);
		up.setUserId(user1.getId().intValue());
		userPhotoDao.saveUserPhoto(up);

		if (user1 != null) {
			responseJson.put("status",
					"ok");
			responseJson.put("msg",
					"photo added");
		}

		return responseJson;
	}
}
