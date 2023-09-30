package com.farneser.weatherviewer.servlets;

import com.farneser.weatherviewer.dto.api.LocationResponse;
import com.farneser.weatherviewer.helpers.factory.CoordinatesFactory;
import com.farneser.weatherviewer.helpers.parser.ParameterParser;
import com.farneser.weatherviewer.models.Location;
import com.farneser.weatherviewer.servlets.auth.AuthServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Search page", urlPatterns = "/search")
public class SearchServlet extends AuthServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var search = ParameterParser.getString(request, "location");

        List<LocationResponse> locationsResponse = new ArrayList<>();

        if (search != null && !search.isEmpty()) {
            var locations = apiService.getLocationsByName(search);

            locations.forEach(locationResponse -> {
                var locationByCoordinates = locationDao
                        .getByCoordinates(
                                locationResponse.getLatitude(),
                                locationResponse.getLongitude(),
                                session.getUser().getId());
                if (locationByCoordinates == null) {
                    locationsResponse.add(locationResponse);
                }
            });
        }

        context.setVariable("search_field", search);
        context.setVariable("locations", locationsResponse);
        templateEngine.process("search", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var coordinates = CoordinatesFactory.getCoordinates(req);

        var search = ParameterParser.getString(req, "search_field");

        var location = locationDao.getByCoordinates(coordinates.getLat(), coordinates.getLon(), session.getUser().getId());

        if (location == null) {

            var locationResponse = apiService.getWeatherByLocation(coordinates.getLat(), coordinates.getLon());

            location = new Location();

            location.setUser(sessionDao.getById(getSessionId(req)).getUser());
            location.setName(locationResponse.getName() + ", " + locationResponse.getSys().getCountry());
            location.setLatitude(coordinates.getLat());
            location.setLongitude(coordinates.getLon());

            locationDao.create(location);

            resp.sendRedirect("?location=" + search);
            return;
        }

        templateEngine.process("search", context, resp.getWriter());
    }
}
