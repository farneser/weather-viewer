package com.farneser.weatherviewer.dao.location;

import com.farneser.weatherviewer.dao.EntityDaoMock;
import com.farneser.weatherviewer.models.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationDaoMock extends EntityDaoMock<Location, Integer> implements ILocationDao {

    private static final ArrayList<Location> locationData = new ArrayList<>();

    @Override
    public Location getById(Integer id) {
        for (var location : get()) {
            if (location.getId() == id) {
                return location;
            }
        }

        return null;
    }

    @Override
    public void delete(Integer id) {
        get().removeIf(location -> location.getId() == id);
    }

    @Override
    public List<Location> get() {
        return locationData;
    }
}
