package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.UserPhotoDao;
import org.androidpn.server.model.UserPhoto;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class UserPhotoDaoHibernate extends HibernateDaoSupport implements UserPhotoDao {

	@Override
	public UserPhoto getUserPhotoByUserId(
			Long id) {
		List<UserPhoto> userPhotoList = getHibernateTemplate().find("from UserPhoto u where u.userId=" + id);
		if (userPhotoList == null || userPhotoList.size() == 0)
			return null;
		return userPhotoList.get(0);
	}

	@Override
	public UserPhoto saveUserPhoto(
			UserPhoto userPhoto) {
		getHibernateTemplate().saveOrUpdate(userPhoto);
		getHibernateTemplate().flush();
		return userPhoto;
	}

}
