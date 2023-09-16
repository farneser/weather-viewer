package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.models.Location;
import org.hibernate.Session;

import java.util.List;

public class LocationDao extends EntityDao<Location, Integer> {
    public LocationDao(Session session) {
        super(session, Location.class);
    }

    public List<Location> get() {
        return session.createQuery("FROM Location", Location.class).list();
    }
}