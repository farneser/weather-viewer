package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.models.Session;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SessionDao extends EntityDao<Session, UUID> {
    public SessionDao(org.hibernate.Session session) {
        super(session, Session.class);
    }

    @Override
    public List<Session> get() {
        return session.createSelectionQuery("FROM Session", Session.class).list();
    }

    public void cleanUserSessions(int userId) {
        var transaction = session.beginTransaction();

        try {
            var sessionsToDelete = session.createQuery("FROM Session s WHERE s.user.id = :userId", Session.class)
                    .setParameter("userId", userId)
                    .list();

            for (var sessionToDelete: sessionsToDelete){
                session.remove(sessionToDelete);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
