package com.farneser.weatherviewer.dao.user;

import com.farneser.weatherviewer.dao.EntityDaoMock;
import com.farneser.weatherviewer.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDaoMock extends EntityDaoMock<User, Integer> implements IUserDao {
    private static final ArrayList<User> userData = new ArrayList<>();

    @Override
    public User getByUsername(String username) {
        for (var user : get()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getById(Integer id) {

        for (var user : get()) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void delete(Integer id) {
        get().removeIf(user -> user.getId() == id);
    }

    @Override
    public List<User> get() {
        return userData;
    }
}
