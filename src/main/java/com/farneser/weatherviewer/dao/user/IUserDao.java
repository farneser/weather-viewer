package com.farneser.weatherviewer.dao.user;

import com.farneser.weatherviewer.dao.IEntityDao;
import com.farneser.weatherviewer.models.User;


public interface IUserDao extends IEntityDao<User, Integer> {
    User getByUsername(String username);
}
