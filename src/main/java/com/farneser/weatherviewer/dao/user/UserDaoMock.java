package com.farneser.weatherviewer.dao.user;

import com.farneser.weatherviewer.dao.EntityDaoMock;
import com.farneser.weatherviewer.models.User;

public class UserDaoMock extends EntityDaoMock<User, Integer> implements IUserDao {
    private static int counter = 0;

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
    protected Integer generateNewId() {
        counter++;

        return counter;
    }
}
