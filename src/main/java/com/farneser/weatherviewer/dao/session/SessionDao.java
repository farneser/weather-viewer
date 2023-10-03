package com.farneser.weatherviewer.dao.session;

import com.farneser.weatherviewer.dao.BaseDao;
import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.factory.HibernateFactory;
import com.farneser.weatherviewer.models.Session;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

public class SessionDao extends BaseDao<Session, UUID> implements ISessionDao {
    private final Logger logger = Logger.getLogger(SessionDao.class.getName());

    public SessionDao() {
        super(Session.class);
    }

    public void cleanUserSessions(int userId) throws InternalServerException {
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

                logger.warning(Arrays.toString(e.getStackTrace()));
            }
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
