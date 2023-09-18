package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.dao.SessionDao;
import com.farneser.weatherviewer.helpers.factory.HibernateFactory;
import com.farneser.weatherviewer.servlets.BaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

public class AuthServlet extends BaseServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var sessionDao = new SessionDao(HibernateFactory.getSessionFactory().openSession());

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
