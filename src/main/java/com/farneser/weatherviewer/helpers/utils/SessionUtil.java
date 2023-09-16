package com.farneser.weatherviewer.helpers.utils;

import com.farneser.weatherviewer.models.Session;
import com.farneser.weatherviewer.models.User;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public abstract class SessionUtil {

    public static Session build(User user) {
        var session = new Session();

        session.setId(UUID.randomUUID());
        session.setUser(user);

        var calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        calendar.add(Calendar.HOUR_OF_DAY, 24);

        session.setExpiresAt(calendar.getTime());

        return session;
    }

}
