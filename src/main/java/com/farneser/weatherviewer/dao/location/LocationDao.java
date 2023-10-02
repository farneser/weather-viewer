package com.farneser.weatherviewer.dao.location;

import com.farneser.weatherviewer.dao.BaseDao;
import com.farneser.weatherviewer.factory.HibernateFactory;
import com.farneser.weatherviewer.models.Location;

import java.util.List;

public class LocationDao extends BaseDao<Location, Integer> implements ILocationDao {
    public LocationDao() {
        super(Location.class);
    }

    public List<Location> get() {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            return session.createSelectionQuery("FROM Location", Location.class).list();
        }
    }

    @Override
    public Location getByCoordinates(double lat, double lon, int userId) {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            return session
                    .createSelectionQuery("FROM Location WHERE latitude = :lat AND longitude = :lon AND user.id = :userId", Location.class)
                    .setParameter("lat", lat)
                    .setParameter("lon", lon)
                    .setParameter("userId", userId)
                    .uniqueResult();
        }
    }

    /**
     * @param userId defines ID of the user whose location we want to get
     * @return list of user locations
     */
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