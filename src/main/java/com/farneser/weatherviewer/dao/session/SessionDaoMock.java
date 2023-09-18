package com.farneser.weatherviewer.dao.session;

import com.farneser.weatherviewer.dao.EntityDaoMock;
import com.farneser.weatherviewer.models.Session;

import java.util.UUID;

public class SessionDaoMock extends EntityDaoMock<Session, UUID> implements ISessionDao {
    @Override
    public void cleanUserSessions(int userId) {
        get().removeIf(session -> session.getUser().getId() == userId);
    }

    @Override
    protected UUID generateNewId() {
        return UUID.randomUUID();
    }
}
