package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.dao.hibernate.Order;
import org.androidpn.server.model.User;
import org.androidpn.server.service.UserNotFoundException;

public interface UserDao {

    public User getUser(Long id) throws UserNotFoundException;

    public User saveUser(User user);

    public void removeUser(Long id) throws UserNotFoundException;

    public boolean exists(Long id);
    
    public int getUserCount();
    
    public List<User> getUsers();

    public List<User> getUsers(String string, Order desc, int i, int j);

    public User getUserByUsername(String username);
    
}
