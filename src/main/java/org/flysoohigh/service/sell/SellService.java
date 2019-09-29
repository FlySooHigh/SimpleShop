package org.flysoohigh.service.sell;

import org.flysoohigh.service.ImportDataService;
import org.springframework.stereotype.Service;

@Service
public class SellService implements ISellService {
    private ImportDataService dataService;

    public SellService(ImportDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public String sell(String itemToSell, String currentUser) {
        if (currentUser.isEmpty()) {
            return "You are not logged in";
        }
        if (!dataService.isItemInShopList(itemToSell)) {
            return "This item is not in shop list, so you could not buy it";
        }
        if (!dataService.isInTheCustomerList(currentUser, itemToSell)) {
            return "This item is not in your bought items' list";
        }
        dataService.sellItem(currentUser, itemToSell);
        return "Item sold successfully!";
    }
}
