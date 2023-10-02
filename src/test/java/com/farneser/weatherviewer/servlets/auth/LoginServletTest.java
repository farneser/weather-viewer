package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.dao.session.ISessionDao;
import com.farneser.weatherviewer.dao.user.IUserDao;
import com.farneser.weatherviewer.utils.PasswordUtil;
import com.farneser.weatherviewer.models.Session;
import com.farneser.weatherviewer.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    @Mock
    private IUserDao userDao;

    @Mock
    private ISessionDao sessionDao;
    @Captor
    private ArgumentCaptor<String> redirectUrlCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoPostWithValidCredentials() throws Exception {
        var user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword(PasswordUtil.hashPassword("password"));

        var session = new Session();
        session.setId(UUID.randomUUID());
        session.setExpiresAt(new Date(System.currentTimeMillis() + 3600000));

        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password");
        when(userDao.getByUsername("testuser")).thenReturn(user);
        when(sessionDao.create(any(Session.class))).thenReturn(session);

        var servlet = new LoginServlet();
        servlet.setUserDao(userDao);
        servlet.setSessionDao(sessionDao);

        servlet.doPost(request, response);

        verify(response).sendRedirect(redirectUrlCaptor.capture());

        var capturedRedirectUrl = redirectUrlCaptor.getValue();
        var expectedRedirectUrl = request.getContextPath();

        assertEquals(expectedRedirectUrl, capturedRedirectUrl);
    }

    @Test
    public void testDoPostWithInvalidCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("nonexistentuser");
        when(request.getParameter("password")).thenReturn("incorrectpassword");
        when(userDao.getByUsername("nonexistentuser")).thenReturn(null);

        var servlet = new LoginServlet();
        servlet.setUserDao(userDao);
        servlet.setSessionDao(sessionDao);

        servlet.doPost(request, response);

        verify(response).sendRedirect(redirectUrlCaptor.capture());

        var capturedRedirectUrl = redirectUrlCaptor.getValue();

        assertEquals(capturedRedirectUrl, "login");
    }
}
