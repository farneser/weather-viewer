package com.farneser.weatherviewer.servlets;

import com.farneser.weatherviewer.dto.api.WeatherResponse;
import com.farneser.weatherviewer.dto.api.weather.Coordinates;
import com.farneser.weatherviewer.helpers.factory.CoordinatesFactory;
import com.farneser.weatherviewer.servlets.auth.AuthServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "Dashboard page", urlPatterns = "/dashboard")
public class HomeServlet extends AuthServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var cities = new ArrayList<WeatherResponse>();

        var locations = locationDao.getByUserId(session.getUser().getId());

        locations.forEach(location -> {
            var weather = apiService.getWeatherByLocation(location.getLatitude(), location.getLongitude());
            var coordinates = new Coordinates();

            coordinates.setLat(location.getLatitude());
            coordinates.setLon(location.getLongitude());

            weather.setCoordinates(coordinates);

            cities.add(weather);
        });

        context.setVariable("cities", cities);

        templateEngine.process("home", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var coordinates = CoordinatesFactory.getCoordinates(req);

        var location = locationDao.getByCoordinates(
                coordinates.getLat(),
                coordinates.getLon(),
                session.getUser().getId());


        if (location != null) {
            locationDao.delete(location.getId());
        }

        resp.sendRedirect("dashboard");
    }
}
