package com.farneser.weatherviewer.dao.location;

import com.farneser.weatherviewer.dao.EntityDaoMock;
import com.farneser.weatherviewer.models.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationDaoMock extends EntityDaoMock<Location, Integer> implements ILocationDao {
    private static int counter = 0;

    @Override
    protected Integer generateNewId() {
        counter++;

        return counter;
    }

    @Override
    public Location getByCoordinates(double lat, double lon) {

        for (var location : get()) {
            if (location.getLatitude() == lat && location.getLongitude() == lon) {
                return location;
            }
        }

        return null;
    }

    @Override
    public List<Location> getByUserId(int userId) {

        var result = new ArrayList<Location>();

        for (var location : get()) {
            if (location.getUser().getId() == userId) {
                result.add(location);
            }
        }

        return result;
    }
}
