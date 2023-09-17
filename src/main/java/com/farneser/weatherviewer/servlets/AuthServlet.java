package com.farneser.weatherviewer.servlets;

import com.farneser.weatherviewer.dao.SessionDao;
import com.farneser.weatherviewer.helpers.factory.HibernateFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

public class AuthServlet extends BaseServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var sessionDao = new SessionDao(HibernateFactory.getSessionFactory().openSession());

        var session = sessionDao.getById(getSessionId(req));

        if (session != null && session.getExpiresAt().getTime() > new Date().getTime()) {
            super.service(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }
}
