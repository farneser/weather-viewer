package com.farneser.weatherviewer.servlets;

import com.farneser.weatherviewer.helpers.factory.ThymeleafFactory;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseServlet extends HttpServlet {
    protected WebContext context;
    protected ITemplateEngine templateEngine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = ThymeleafFactory.getInstance();

        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context = ThymeleafFactory.buildWebContext(req, resp, getServletContext());

        super.service(req, resp);
    }

    protected UUID getSessionId(HttpServletRequest request) {
        var cookie = findSessionCookie(request.getCookies());

        return cookie.map(value -> UUID.fromString(value.getValue())).orElse(null);
    }

    private static Optional<Cookie> findSessionCookie(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }

        return Arrays
                .stream(cookies)
                .filter(cookie -> cookie
                        .getName()
                        .equals("sessionId"))
                .findFirst();
    }
}
