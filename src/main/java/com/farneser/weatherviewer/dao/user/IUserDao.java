package com.farneser.weatherviewer.dao.user;

import com.farneser.weatherviewer.dao.IEntityDao;
import com.farneser.weatherviewer.models.User;

import java.util.List;

public interface IUserDao extends IEntityDao<User, Integer> {
    User getByUsername(String username);

    public List<User> get();
}
