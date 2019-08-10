package org.flysoohigh.service.login;

import org.flysoohigh.service.ImportDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class LoginServiceTest {
    private final static String NO_LOGGED_IN_USER = "";
    private final static String EMPTY_NEW_USER = "";
    private LoginService loginService;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private ImportDataService importDataService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        loginService = new LoginService(printWriter, importDataService);
    }

    @Test
    void testLoginNewUserIsEmpty() {
        String result = loginService.login(EMPTY_NEW_USER, NO_LOGGED_IN_USER);

        verify(printWriter).println("You have to specify customer name after login");

        assertEquals(NO_LOGGED_IN_USER, result);
    }

    @Test
    void testLoginUserIsNotRegistered() {
        when(importDataService.isCustomer("notRegisteredUser")).thenReturn(false);

        String result = loginService.login("notRegisteredUser", NO_LOGGED_IN_USER);

        verify(printWriter).println("Such user is not registered. Enter existing user name.");
        assertEquals(NO_LOGGED_IN_USER, result);
    }

    @Test
    void testLoginLoggedInOtherTerminal() {
        when(importDataService.isCustomer("validUser")).thenReturn(true);

        String result = loginService.login("validUser", "someOtherUser");

        verify(printWriter).println("Some user is already logged in this terminal");
        assertEquals("someOtherUser", result);
    }

    @Test
    void testLoginAlreadyLoggedIn() {
        when(importDataService.isCustomer("validUser")).thenReturn(true);
        when(importDataService.isLoggedIn("validUser")).thenReturn(true);

        String result = loginService.login("validUser", NO_LOGGED_IN_USER);

        verify(printWriter).println("You are already logged in another terminal");
        assertEquals(NO_LOGGED_IN_USER, result);
    }

    @Test
    void testLoginSuccess() {
        when(importDataService.isCustomer("validUser")).thenReturn(true);
        when(importDataService.isLoggedIn("validUser")).thenReturn(false);

        String result = loginService.login("validUser", NO_LOGGED_IN_USER);

        verify(importDataService).logIn("validUser");
        verify(printWriter).println("Login successful");
        assertEquals("validUser", result);
    }
}