package com.farneser.weatherviewer.dao.session;

import com.farneser.weatherviewer.dao.IEntityDao;
import com.farneser.weatherviewer.models.Session;

import java.util.UUID;

public interface ISessionDao extends IEntityDao<Session, UUID> {
    void cleanUserSessions(int userId);
}
