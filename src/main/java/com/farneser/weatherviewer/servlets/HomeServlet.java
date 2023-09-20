package com.farneser.weatherviewer.servlets;

import com.farneser.weatherviewer.servlets.auth.AuthServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Home page", urlPatterns = "/home")
public class HomeServlet extends AuthServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var session = sessionDao.getById(getSessionId(request));

        var user = session.getUser();

        var locations = locationDao.getByUserId(user.getId());

        context.setVariable("locations", locations);

        templateEngine.process("home", context, response.getWriter());
    }

}
