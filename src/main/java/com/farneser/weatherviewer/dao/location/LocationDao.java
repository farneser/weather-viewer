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
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            return session.createSelectionQuery("FROM Location", Location.class).list();
        }
    }

    @Override
    public Location getByCoordinates(double lat, double lon) {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            return session
                    .createSelectionQuery("FROM Location WHERE latitude = :lat AND longitude = :lon", Location.class)
                    .setParameter("lat", lat)
                    .setParameter("lon", lon)
                    .uniqueResult();
        }
    }

    @Override
    public List<Location> getByUserId(int userId) {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            return session
                    .createSelectionQuery("FROM Location WHERE user.id = :userId", Location.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }
}