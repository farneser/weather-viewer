package com.farneser.weatherviewer.servlets;

import com.farneser.weatherviewer.dto.api.WeatherResponse;
import com.farneser.weatherviewer.servlets.auth.AuthServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "Home page", urlPatterns = "/home")
public class HomeServlet extends AuthServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var session = sessionDao.getById(getSessionId(request));

        var user = session.getUser();

        var cities = new ArrayList<WeatherResponse>();

        var locations = locationDao.getByUserId(user.getId());

        locations.forEach(location -> cities.add(apiService.getWeatherByLocation(location.getLatitude(), location.getLongitude())));

        context.setVariable("cities", cities);

        templateEngine.process("home", context, response.getWriter());
    }


}
