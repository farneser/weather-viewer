package com.farneser.weatherviewer.dao.location;

import com.farneser.weatherviewer.dao.EntityDaoMock;
import com.farneser.weatherviewer.models.Location;

public class LocationDaoMock extends EntityDaoMock<Location, Integer> implements ILocationDao {
    private static int counter = 0;

    @Override
    protected Integer generateNewId() {
        counter++;

        return counter;
    }
}
