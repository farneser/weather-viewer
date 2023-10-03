package com.farneser.weatherviewer.dao.user;

import com.farneser.weatherviewer.dao.IBaseDao;
import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.models.User;


public interface IUserDao extends IBaseDao<User, Integer> {
    User getByUsername(String username) throws InternalServerException;
}
