package com.farneser.weatherviewer.servlets;

import com.farneser.weatherviewer.dao.location.ILocationDao;
import com.farneser.weatherviewer.dao.location.LocationDao;
import com.farneser.weatherviewer.dao.session.ISessionDao;
import com.farneser.weatherviewer.dao.session.SessionDao;
import com.farneser.weatherviewer.dao.user.IUserDao;
import com.farneser.weatherviewer.dao.user.UserDao;
import com.farneser.weatherviewer.factory.ThymeleafFactory;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.UUID;

public abstract class BaseServlet extends HttpServlet {
    protected static final String authCookieName = "sessionId";
    @Setter
    protected WebContext context;

    @Getter
    @Setter
    protected ISessionDao sessionDao = new SessionDao();

    @Getter
    @Setter
    protected IUserDao userDao = new UserDao();

    @Getter
    @Setter
    protected ILocationDao locationDao = new LocationDao();

    @Getter
    @Setter
    protected ITemplateEngine templateEngine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = ThymeleafFactory.getInstance();

        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context = ThymeleafFactory.buildWebContext(req, resp, getServletContext());

        this.service(req, resp, context);
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp, WebContext context) throws ServletException, IOException {
        this.context = context;

        super.service(req, resp);
    }

    protected UUID getSessionId(HttpServletRequest request) {
        var cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (var c : cookies) {
            if (c.getName().equals(authCookieName)) {
                return UUID.fromString(c.getValue());
            }
        }

        return null;
    }
}
