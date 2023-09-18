package com.farneser.weatherviewer.dao.location;

import com.farneser.weatherviewer.dao.IEntityDao;
import com.farneser.weatherviewer.models.Location;

import java.util.List;

public interface ILocationDao extends IEntityDao<Location, Integer> {
    List<Location> get();
}