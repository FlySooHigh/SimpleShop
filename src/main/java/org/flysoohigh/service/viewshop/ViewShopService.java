package org.flysoohigh.service.viewshop;

import org.flysoohigh.model.Item;
import org.flysoohigh.service.ImportDataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewShopService implements IViewShopService {
    private ImportDataService dataService;

    public ViewShopService(ImportDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public List<Item> showItems() {
            return dataService.getAllShopItems();
    }
}
