package org.flysoohigh.service.sell;

import org.flysoohigh.service.ImportDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.PrintWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class SellServiceTest {
    private final static String NO_LOGGED_IN_USER = "";
    private SellService sellService;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private ImportDataService importDataService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        sellService = new SellService(printWriter, importDataService);
    }

    @Test
    void testSellUserIsEmpty() {
        sellService.sell("book", NO_LOGGED_IN_USER);
        verify(printWriter).println("You are not logged in");
    }

    @Test
    void testSellItemNotInShopList() {
        when(importDataService.isItemInShopList("book")).thenReturn(false);
        sellService.sell("book", "someUser");
        verify(printWriter).println("This item is not in shop list, so you could not buy it");
    }

    @Test
    void testSellItemNotInBoughtList() {
        when(importDataService.isItemInShopList("book")).thenReturn(true);
        when(importDataService.isInTheCustomerList("someUser", "book")).thenReturn(false);
        sellService.sell("book", "someUser");
        verify(printWriter).println("This item is not in your bought items' list");
    }

    @Test
    void testSellSuccess() {
        when(importDataService.isItemInShopList("book")).thenReturn(true);
        when(importDataService.isInTheCustomerList("someUser", "book")).thenReturn(true);
        sellService.sell("book", "someUser");
        verify(importDataService).sellItem("someUser", "book");
        verify(printWriter).println("Item sold successfully!");
    }
}