package org.androidpn.server.dao;

import org.androidpn.server.model.UserPhoto;

public interface UserPhotoDao {

    public UserPhoto getUserPhotoByUserId(Long id);

    public UserPhoto saveUserPhoto(UserPhoto user);

}
