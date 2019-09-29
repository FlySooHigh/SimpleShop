//package org.flysoohigh.service.logout;
//
//import org.flysoohigh.service.ImportDataService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//
//import java.io.PrintWriter;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//class LogoutServiceTest {
//    private final static String NO_LOGGED_IN_USER = "";
//    private LogoutService logoutService;
//    @Mock
//    private PrintWriter printWriter;
//    @Mock
//    private ImportDataService importDataService;
//
//    @BeforeEach
//    void setUp() {
//        initMocks(this);
//        logoutService = new LogoutService(printWriter, importDataService);
//    }
//
//    @Test
//    void testLogoutUserIsEmpty() {
//        logoutService.logout(NO_LOGGED_IN_USER);
//        verify(printWriter).println("You are not logged in");
//    }
//
//    @Test
//    void testLogoutSuccess() {
//        String result = logoutService.logout("someUser");
//        verify(importDataService).logOut("someUser");
//        verify(printWriter).println("Logout successful");
//        assertEquals(NO_LOGGED_IN_USER, result);
//    }
//}