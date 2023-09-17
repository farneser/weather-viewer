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
        var cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (var c : cookies) {
            if (c.getName().equals("sessionId")) {
                return UUID.fromString(c.getValue());
            }
        }

        return null;
    }
}
