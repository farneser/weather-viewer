package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.dao.user.UserDao;
import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.models.StaticDaoData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.logging.Logger;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegisterServletTest {
    private final Logger logger = Logger.getLogger(RegisterServletTest.class.getName());

    private final UserDao userDao = new UserDao();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private RegisterServlet servlet;
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        servlet.setUserDao(userDao);

        for (var user : StaticDaoData.getUsers()) {
            try {
                userDao.create(user);
            } catch (InternalServerException e) {
                logger.warning(e.getMessage());
            }
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testDoPostWithMatchingPasswords() throws Exception {
        when(request.getParameter("username")).thenReturn("testuser1");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("password");

        try {
            servlet.doPost(request, response);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        assert userDao.getByUsername("testuser1") != null;

        verify(response).sendRedirect("login");
    }

    @Test
    public void testDoPostWithNonMatchingPasswords() throws Exception {
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("differentpassword");
        try {
            servlet.doPost(request, response);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        assert userDao.getByUsername("newuser") == null;
    }

    @Test
    public void testDoPostWithExistingUser() throws Exception {

        assert userDao.getByUsername("Nikolay").getId() == 1;

        when(request.getParameter("username")).thenReturn("Nikolay");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("password");

        try {
            servlet.doPost(request, response);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }

        assert userDao.getByUsername("Nikolay").getId() == 1;
    }
}
