package com.farneser.weatherviewer.dao.location;

import com.farneser.weatherviewer.dao.EntityDao;
import com.farneser.weatherviewer.helpers.factory.HibernateFactory;
import com.farneser.weatherviewer.models.Location;

import java.util.List;

public class LocationDao extends EntityDao<Location, Integer> implements ILocationDao {
    public LocationDao() {
        super(Location.class);
    }

    public List<Location> get() {
        var session = HibernateFactory.getSessionFactory().openSession();

        return session.createSelectionQuery("FROM Location", Location.class).list();
    }
}