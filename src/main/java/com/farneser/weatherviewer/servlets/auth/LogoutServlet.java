package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.servlets.BaseServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "Logout page", urlPatterns = "/logout")
public class LogoutServlet extends BaseServlet {
    private final Logger logger = Logger.getLogger(LogoutServlet.class.getName());

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
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

            var session = sessionDao.getById(getSessionId(req));

            sessionDao.cleanUserSessions(session.getUser().getId());

            resp.sendRedirect("login");
        } catch (InternalServerException e) {
            logger.warning(e.getMessage());
        }
    }
}
