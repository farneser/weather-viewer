package com.farneser.weatherviewer.servlets;

import com.farneser.weatherviewer.dao.UserDao;
import com.farneser.weatherviewer.helpers.factory.HibernateFactory;
import com.farneser.weatherviewer.helpers.factory.UserDtoFactory;
import com.farneser.weatherviewer.helpers.utils.PasswordUtil;
import com.farneser.weatherviewer.models.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;

@WebServlet(name = "Register page", urlPatterns = "/register")
public class RegisterServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        templateEngine.process("register", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var registerDto = UserDtoFactory.getRegister(request);

        var userDao = new UserDao(HibernateFactory.getSessionFactory().openSession());

        if (registerDto.getPassword().equals(registerDto.getConfirmPassword())) {

            try {
                var user = new User();

                user.setUsername(registerDto.getUsername());
                user.setPassword(PasswordUtil.hashPassword(registerDto.getPassword()));

                userDao.create(user);
                response.sendRedirect("login");

            } catch (ConstraintViolationException e) {
                context.setVariable("errorMessage", "There is already a user with this name");
                templateEngine.process("register", context, response.getWriter());
            }

        } else {
            context.setVariable("errorMessage", "There is already a user with this name");
            templateEngine.process("register", context, response.getWriter());
        }
    }
}
