package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.helpers.factory.ThymeleafFactory;
import com.farneser.weatherviewer.models.Session;
import com.farneser.weatherviewer.services.OpenWeatherApiService;
import com.farneser.weatherviewer.servlets.BaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

public abstract class AuthServlet extends BaseServlet {
    protected OpenWeatherApiService apiService = new OpenWeatherApiService();
    protected Session session;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            var session = sessionDao.getById(getSessionId(req));

            if (session != null && session.getExpiresAt().getTime() > new Date().getTime()) {
                var user = session.getUser();

                context = ThymeleafFactory.buildWebContext(req, resp, getServletContext());
                context.setVariable("user", user.getUsername());

                this.session = session;

                super.service(req, resp, context);
            }
        } catch (Exception d) {
            resp.sendRedirect("login");
        }

    }
}
