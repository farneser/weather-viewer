package com.farneser.weatherviewer.helpers.factory;

import com.farneser.weatherviewer.dto.api.weather.Coordinates;
import com.farneser.weatherviewer.helpers.parser.ParameterParser;
import jakarta.servlet.http.HttpServletRequest;

public abstract class CoordinatesFactory {
    public static Coordinates getCoordinates(HttpServletRequest request) {
        var coordinates = new Coordinates();

        coordinates.setLat(ParameterParser.getDouble(request, "lat"));
        coordinates.setLon(ParameterParser.getDouble(request, "lon"));

        return coordinates;
    }
}
