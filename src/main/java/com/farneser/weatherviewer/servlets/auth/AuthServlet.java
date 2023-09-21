package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.helpers.factory.ThymeleafFactory;
import com.farneser.weatherviewer.models.Session;
import com.farneser.weatherviewer.services.OpenWeatherApiService;
import com.farneser.weatherviewer.servlets.BaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;

import java.io.IOException;
import java.util.Arrays;
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
                return;
            }

            throw new NotFoundException();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));

            resp.sendRedirect("login");
        }

    }
}
