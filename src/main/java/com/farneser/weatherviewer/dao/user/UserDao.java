package com.farneser.weatherviewer.dao.user;

import com.farneser.weatherviewer.dao.EntityDao;
import com.farneser.weatherviewer.factory.HibernateFactory;
import com.farneser.weatherviewer.models.User;

import java.util.List;

public class UserDao extends EntityDao<User, Integer> implements IUserDao {
    public UserDao() {
        super(User.class);
    }

    public User getByUsername(String username) {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            return session.createSelectionQuery("FROM User WHERE lower(username) = lower(:username)", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    @Override
    public List<User> get() {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            return session.createSelectionQuery("FROM User", User.class).list();
        }
    }
}
