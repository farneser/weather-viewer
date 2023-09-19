package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.servlets.BaseServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Logout page", urlPatterns = "/logout")
public class LogoutServlet extends BaseServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(authCookieName)) {
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                    break;
                }
            }
        }

        try {
            var session = sessionDao.getById(getSessionId(req));

            sessionDao.cleanUserSessions(session.getUser().getId());
        } catch (Exception ignored) {
        }

        resp.sendRedirect("login");
    }
}
