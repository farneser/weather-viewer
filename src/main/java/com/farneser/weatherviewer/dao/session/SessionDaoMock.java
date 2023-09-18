package com.farneser.weatherviewer.dao.session;

import com.farneser.weatherviewer.dao.EntityDaoMock;
import com.farneser.weatherviewer.models.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionDaoMock extends EntityDaoMock<Session, UUID> implements ISessionDao {
    private static final ArrayList<Session> sessionData = new ArrayList<>();

    @Override
    public Session getById(UUID id) {
        for (var session : get()) {
            if (session.getId() == id) {
                return session;
            }
        }

        return null;
    }

    @Override
    public void delete(UUID id) {
        get().removeIf(session -> session.getId() == id);
    }

    @Override
    public List<Session> get() {
        return sessionData;
    }

    @Override
    public void cleanUserSessions(int userId) {
        get().removeIf(session -> session.getUser().getId() == userId);
    }
}
