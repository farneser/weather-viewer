package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.dao.SessionDao;
import com.farneser.weatherviewer.dao.UserDao;
import com.farneser.weatherviewer.helpers.factory.HibernateFactory;
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
        var loginDto = UserDtoFactory.getLogin(request);

        var userDao = new UserDao(HibernateFactory.getSessionFactory().openSession());

        User user = null;

        if (loginDto.getUsername() != null) {
            user = userDao.getByUsername(loginDto.getUsername());
        }

        if (user == null || !PasswordUtil.isPasswordCorrect(loginDto.getPassword(), user.getPassword())) {

            context.setVariable("errorMessage", "The username or password you entered is incorrect");
            templateEngine.process("login", context, response.getWriter());

            return;
        }

        var sessionDao = new SessionDao(HibernateFactory.getSessionFactory().openSession());

        sessionDao.cleanUserSessions(user.getId());

        var session = SessionUtil.build(user);

        session = sessionDao.create(session);

        var cookie = new Cookie(authCookieName, session.getId().toString());

        cookie.setMaxAge((int) ((session.getExpiresAt().getTime() - System.currentTimeMillis()) / 1000));

        response.addCookie(cookie);

        response.sendRedirect("");
    }
}
