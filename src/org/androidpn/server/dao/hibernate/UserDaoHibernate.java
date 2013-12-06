package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.UserDao;
import org.androidpn.server.model.User;
import org.androidpn.server.service.UserNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class UserDaoHibernate extends HibernateDaoSupport implements UserDao {

	public User getUser(Long id) throws UserNotFoundException {
		User u = (User) getHibernateTemplate().get(User.class, id);
		if (u == null) {
			throw new UserNotFoundException("User.id=" + id + " not found");
		}

		return u;
	}

	public User saveUser(User user) {
		getHibernateTemplate().saveOrUpdate(user);
		getHibernateTemplate().flush();
		return user;
	}

	public void removeUser(Long id) throws UserNotFoundException {
		getHibernateTemplate().delete(getUser(id));
	}

	public boolean exists(Long id) {
		User user = (User) getHibernateTemplate().get(User.class, id);
		return user != null;
	}

	public int getUserCount() {
		String hql = "select count(*) from User u";
		Session s = getHibernateTemplate().getSessionFactory().openSession();
		Query q = s.createQuery(hql);
		int count = ((Long) q.iterate().next()).intValue();
		s.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsers() {
		return getHibernateTemplate().find("from User u order by u.createdDate desc");
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsers(String orderType, Order order, int start, int length) {
		Session s = getHibernateTemplate().getSessionFactory().openSession();
		Query q = s.createQuery("from User u order by u." + orderType + " " + order);
		q.setFirstResult(start);
		q.setMaxResults(length);
		List<User> users = q.list();
		s.close();
		return users;
	}

	public User getUserByUsername(String username) {
		List<?> users = getHibernateTemplate().find("from User where username=?", username);
		if (users == null || users.isEmpty()) {
			return null;
		} else {
			return (User) users.get(0);
		}
	}
}
