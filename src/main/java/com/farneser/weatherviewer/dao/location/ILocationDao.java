package com.farneser.weatherviewer.dao.location;

import com.farneser.weatherviewer.dao.IBaseDao;
import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.models.Location;

import java.util.List;

public interface ILocationDao extends IBaseDao<Location, Integer> {
    Location getByCoordinates(double lat, double lon, int userId) throws InternalServerException;

    List<Location> getByUserId(int userId) throws InternalServerException;
}