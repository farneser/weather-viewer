package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.models.Session;

import java.util.List;
import java.util.UUID;

public class SessionDao extends EntityDao<Session, UUID> {
    public SessionDao(org.hibernate.Session session) {
        super(session, Session.class);
    }

    @Override
    public List<Session> get() {
        return session.createQuery("FROM Session", Session.class).list();
    }
}