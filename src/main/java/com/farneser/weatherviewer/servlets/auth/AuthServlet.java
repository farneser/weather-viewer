package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.services.OpenWeatherApiService;
import com.farneser.weatherviewer.servlets.BaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

public abstract class AuthServlet extends BaseServlet {
    protected OpenWeatherApiService apiService = new OpenWeatherApiService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            var session = sessionDao.getById(getSessionId(req));

            if (session != null && session.getExpiresAt().getTime() > new Date().getTime()) {
                super.service(req, resp);
            }
        } catch (Exception d) {
            resp.sendRedirect("login");
        }

    }
}
