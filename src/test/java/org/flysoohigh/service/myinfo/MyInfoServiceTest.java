//package org.flysoohigh.service.myinfo;
//
//import org.flysoohigh.model.Customer;
//import org.flysoohigh.service.ImportDataService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//
//import java.io.PrintWriter;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//class MyInfoServiceTest {
//    private final static String NO_LOGGED_IN_USER = "";
//    private MyInfoService myInfoService;
//    @Mock
//    private PrintWriter printWriter;
//    @Mock
//    private ImportDataService importDataService;
//
//    @BeforeEach
//    void setUp() {
//        initMocks(this);
//        myInfoService = new MyInfoService(printWriter, importDataService);
//    }
//
//    @Test
//    void testShowInfoEmptyUser() {
//        myInfoService.showInfo(NO_LOGGED_IN_USER);
//        verify(printWriter).println("You are not logged in");
//    }
//
//    @Test
//    void testShowInfoSuccess() {
//        Customer customer = new Customer();
//        when(importDataService.getInfo("someUser")).thenReturn(customer);
//        myInfoService.showInfo("someUser");
//        verify(printWriter).println(customer.toString());
//    }
//}