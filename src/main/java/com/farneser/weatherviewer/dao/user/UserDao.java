package com.farneser.weatherviewer.dao.user;

import com.farneser.weatherviewer.dao.BaseDao;
import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.factory.HibernateFactory;
import com.farneser.weatherviewer.models.User;

public class UserDao extends BaseDao<User, Integer> implements IUserDao {
    public UserDao() {
        super(User.class);
    }

    public User getByUsername(String username) throws InternalServerException {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            return session.createSelectionQuery("FROM User WHERE lower(username) = lower(:username)", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
