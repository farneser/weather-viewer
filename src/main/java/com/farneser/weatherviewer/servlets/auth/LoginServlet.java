package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.exceptions.ParamNotExistsException;
import com.farneser.weatherviewer.helpers.factory.UserDtoFactory;
import com.farneser.weatherviewer.helpers.utils.PasswordUtil;
import com.farneser.weatherviewer.helpers.utils.SessionUtil;
import com.farneser.weatherviewer.models.User;
import com.farneser.weatherviewer.servlets.BaseServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
            var loginDto = UserDtoFactory.getLogin(request);

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

            var session = SessionUtil.build(user);

            session = sessionDao.create(session);

            var cookie = new Cookie(authCookieName, session.getId().toString());

            cookie.setMaxAge((int) ((session.getExpiresAt().getTime() - System.currentTimeMillis()) / 1000));

            response.addCookie(cookie);

            response.sendRedirect("home");
        } catch (ParamNotExistsException e) {
            context.setVariable("errorMessage", e.getMessage());
            templateEngine.process("login", context, response.getWriter());
        }
    }
}
