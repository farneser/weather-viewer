package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.exceptions.ParamNotExistsException;
import com.farneser.weatherviewer.models.Session;
import com.farneser.weatherviewer.models.User;
import com.farneser.weatherviewer.servlets.BaseServlet;
import com.farneser.weatherviewer.utils.PasswordUtil;
import com.farneser.weatherviewer.utils.RequestDataParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@WebServlet(name = "Login page", urlPatterns = "/login")
public class LoginServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        templateEngine.process("login", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            var loginDto = RequestDataParser.getLoginDto(request);

            User user = null;

            if (loginDto.getUsername() != null) {
                user = userDao.getByUsername(loginDto.getUsername());
            }

            if (user == null || !PasswordUtil.isPasswordCorrect(loginDto.getPassword(), user.getPassword())) {
                try {
                    context.setVariable("errorMessage", "The username or password you entered is incorrect");
                    templateEngine.process("login", context, response.getWriter());
                } catch (NullPointerException e) {
                    response.sendRedirect("login");
                }

                return;
            }

            sessionDao.cleanUserSessions(user.getId());

            var session = buildSession(user);

            session = sessionDao.create(session);

            var cookie = new Cookie(authCookieName, session.getId().toString());

            cookie.setMaxAge((int) ((session.getExpiresAt().getTime() - System.currentTimeMillis()) / 1000));

            response.addCookie(cookie);

            response.sendRedirect("dashboard");
        } catch (ParamNotExistsException e) {
            context.setVariable("errorMessage", e.getMessage());
            templateEngine.process("login", context, response.getWriter());
        }
    }

    protected Session buildSession(User user) {
        var session = new Session();

        session.setUser(user);

        var calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        calendar.add(Calendar.HOUR_OF_DAY, 24);

        session.setExpiresAt(calendar.getTime());

        return session;
    }
}
