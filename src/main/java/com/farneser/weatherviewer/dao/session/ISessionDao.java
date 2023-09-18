package com.farneser.weatherviewer.dao.session;

import com.farneser.weatherviewer.dao.IEntityDao;
import com.farneser.weatherviewer.models.Session;

import java.util.List;
import java.util.UUID;

public interface ISessionDao extends IEntityDao<Session, UUID> {
    List<Session> get();

    void cleanUserSessions(int userId);
}
