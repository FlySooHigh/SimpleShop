package org.flysoohigh.service.viewshop;

import org.flysoohigh.model.Item;
import org.flysoohigh.service.ImportDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.PrintWriter;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class ViewShopServiceTest {
    private ViewShopService viewShopService;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private ImportDataService importDataService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        viewShopService = new ViewShopService(printWriter, importDataService);
    }

    @Test
    void testShowItems() {
        when(importDataService.getAllShopItems()).thenReturn(Arrays.asList(new Item(), new Item(), new Item()));
        viewShopService.showItems();
        verify(printWriter, times(3)).println(anyString());
    }
}