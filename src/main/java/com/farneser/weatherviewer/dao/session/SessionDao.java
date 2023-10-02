package com.farneser.weatherviewer.dao.session;

import com.farneser.weatherviewer.dao.EntityDao;
import com.farneser.weatherviewer.factory.HibernateFactory;
import com.farneser.weatherviewer.models.Session;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SessionDao extends EntityDao<Session, UUID> implements ISessionDao {
    public SessionDao() {
        super(Session.class);
    }

    public List<Session> get() {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            return session.createSelectionQuery("FROM Session", Session.class).list();
        }
    }

    public void cleanUserSessions(int userId) {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            var transaction = session.beginTransaction();

            try {
                var sessionsToDelete = session.createQuery("FROM Session WHERE user.id = :userId", Session.class)
                        .setParameter("userId", userId)
                        .list();

                for (var sessionToDelete : sessionsToDelete) {
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
}
