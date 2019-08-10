package org.flysoohigh.service.buy;

import org.flysoohigh.service.ImportDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.PrintWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BuyServiceTest {
    private final static String NO_LOGGED_IN_USER = "";
    private BuyService buyService;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private ImportDataService importDataService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        buyService = new BuyService(printWriter, importDataService);
    }

    @Test
    void testBuyNoUser() {
        buyService.buy("someItem", NO_LOGGED_IN_USER);
        verify(printWriter).println("You are not logged in");
    }

    @Test
    void testBuyItemNotInShop() {
        when(importDataService.isItemInShopList("someItem")).thenReturn(false);

        buyService.buy("someItem", "someUser");

        verify(printWriter).println("This item is not in the shop list");
    }

    @Test
    void testBuyNotEnoughFunds() {
        when(importDataService.isItemInShopList("someItem")).thenReturn(true);
        when(importDataService.getCustomerFunds("someUser")).thenReturn(10);
        when(importDataService.getItemPrice("someItem")).thenReturn(20);

        buyService.buy("someItem", "someUser");

        verify(printWriter).println("Sorry, not enough funds to purchase this item");
    }

    @Test
    void testBuySuccess() {
        when(importDataService.isItemInShopList("someItem")).thenReturn(true);
        when(importDataService.getCustomerFunds("someUser")).thenReturn(20);
        when(importDataService.getItemPrice("someItem")).thenReturn(10);

        buyService.buy("someItem", "someUser");

        verify(importDataService).buyItem("someUser", "someItem", 10);
        verify(printWriter).println("Purchase is successful!");
    }
}