package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.models.User;
import org.hibernate.Session;

import java.util.List;

public class UserDao extends EntityDao<User, Integer> {
    public UserDao(Session session) {
        super(session, User.class);
    }

    public User getByUsername(String username) {
        return session.createSelectionQuery("FROM User WHERE lower(username) = lower(:username)", User.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    @Override
    public List<User> get() {
        return session.createSelectionQuery("FROM User", User.class).list();
    }
}
