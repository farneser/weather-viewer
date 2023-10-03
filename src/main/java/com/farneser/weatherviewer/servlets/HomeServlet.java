package com.farneser.weatherviewer.servlets;

import com.farneser.weatherviewer.dto.api.WeatherResponse;
import com.farneser.weatherviewer.dto.api.weather.Coordinates;
import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.servlets.auth.AuthServlet;
import com.farneser.weatherviewer.utils.RequestDataParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@WebServlet(name = "Dashboard page", urlPatterns = "/dashboard")
public class HomeServlet extends AuthServlet {
    private final Logger logger = Logger.getLogger(HomeServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {


            var cities = new ArrayList<WeatherResponse>();

            var locations = locationDao.getByUserId(session.getUser().getId());

            locations.forEach(location -> {

                WeatherResponse weather = null;
                try {
                    weather = apiService.getWeatherByLocation(location.getLatitude(), location.getLongitude());
                } catch (InternalServerException e) {
                    context.setVariable("errorMessage", e.getMessage());
                }

                var coordinates = new Coordinates();

                coordinates.setLat(location.getLatitude());
                coordinates.setLon(location.getLongitude());

                if (weather != null) {
                    weather.setCoordinates(coordinates);

                    cities.add(weather);
                }
            });

            context.setVariable("cities", cities);

        } catch (InternalServerException e) {
            logger.warning(e.getMessage());
            context.setVariable("errorMessage", e.getMessage());
        }

        templateEngine.process("home", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var coordinates = RequestDataParser.getCoordinates(req);

            var location = locationDao.getByCoordinates(
                    coordinates.getLat(),
                    coordinates.getLon(),
                    session.getUser().getId());


            if (location != null) {
                locationDao.delete(location.getId());
            }

        } catch (InternalServerException e) {
            logger.warning(e.getMessage());
        }

        resp.sendRedirect("dashboard");
    }
}
