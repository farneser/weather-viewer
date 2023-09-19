package com.farneser.weatherviewer.models;

import com.farneser.weatherviewer.helpers.utils.PasswordUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class StaticDaoData {

    public static List<User> getUsers() {
        var list = new ArrayList<User>();

        User user;

        user = new User();
        user.setUsername("Nikolay");
        user.setPassword(PasswordUtil.hashPassword("strong_password"));
        list.add(user);

        user = new User();
        user.setUsername("Egor");
        user.setPassword(PasswordUtil.hashPassword("dwdawoihfaeani"));
        list.add(user);

        user = new User();
        user.setUsername("Tatsiana");
        user.setPassword(PasswordUtil.hashPassword("eopjwafwpjoj3waf"));
        list.add(user);

        user = new User();
        user.setUsername("Vyacheslav");
        user.setPassword(PasswordUtil.hashPassword("strong_password"));
        list.add(user);

        user = new User();
        user.setUsername("Nikita");
        user.setPassword(PasswordUtil.hashPassword("password"));
        list.add(user);

        return list;
    }
}
