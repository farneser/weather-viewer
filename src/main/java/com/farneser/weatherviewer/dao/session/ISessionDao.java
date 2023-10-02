package com.farneser.weatherviewer.dao.session;

import com.farneser.weatherviewer.dao.IBaseDao;
import com.farneser.weatherviewer.models.Session;

import java.util.UUID;

public interface ISessionDao extends IBaseDao<Session, UUID> {
    void cleanUserSessions(int userId);
}
