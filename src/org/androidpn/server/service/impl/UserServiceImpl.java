package org.androidpn.server.service.impl;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.androidpn.server.dao.UserDao;
import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.model.User;
import org.androidpn.server.service.UserExistsException;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;

public class UserServiceImpl implements UserService {

    protected final Log log = LogFactory.getLog(getClass());

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser(String userId) throws NumberFormatException, UserNotFoundException {
        return userDao.getUser(new Long(userId));
    }
    
    public int getUserCount(){
    	return userDao.getUserCount();
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public User saveUser(User user) throws UserExistsException {
        try {
            return userDao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername()
                    + "' already exists!");
        } catch (EntityExistsException e) { // needed for JPA
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername()
                    + "' already exists!");
        }
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        return (User) userDao.getUserByUsername(username);
    }

    public void removeUser(Long userId) throws UserNotFoundException {
        log.debug("removing user: " + userId);
        userDao.removeUser(userId);
    }

	@Override
	public List<User> getUsers(String string, Order order, int pageIndex, int pageSize) {
		return userDao.getUsers(string, order, pageIndex, pageSize);
	}

}
