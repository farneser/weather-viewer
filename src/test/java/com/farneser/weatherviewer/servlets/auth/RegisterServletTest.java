package com.farneser.weatherviewer.servlets.auth;

import com.farneser.weatherviewer.dao.user.IUserDao;
import com.farneser.weatherviewer.dao.user.UserDaoMock;
import com.farneser.weatherviewer.models.StaticDaoData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegisterServletTest {

    private final IUserDao userDao = new UserDaoMock();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private RegisterServlet servlet;

    @Captor
    private ArgumentCaptor<String> redirectUrlCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet.setUserDao(userDao);

        for (var user : StaticDaoData.getUsers()) {
            userDao.create(user);
        }
    }

    @Test
    public void testDoPostWithMatchingPasswords() throws Exception {
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("password");

        servlet.doPost(request, response);

        assert userDao.getByUsername("newuser") != null;
        verify(response).sendRedirect(redirectUrlCaptor.capture());

        var capturedRedirectUrl = redirectUrlCaptor.getValue();
        var expectedRedirectUrl = "login";

        assertEquals(expectedRedirectUrl, capturedRedirectUrl);
    }

    @Test
    public void testDoPostWithNonMatchingPasswords() throws Exception {
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("differentpassword");

        servlet.doPost(request, response);

        assert userDao.getByUsername("newuser") == null;
    }

    @Test
    public void testDoPostWithExistingUser() throws Exception {

        assert userDao.getByUsername("Nikolay").getId() == 1;

        when(request.getParameter("username")).thenReturn("Nikolay");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("password");

        servlet.doPost(request, response);

        assert userDao.getByUsername("Nikolay").getId() == 1;
    }
}
