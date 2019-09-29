package org.flysoohigh.service.buy;

import org.flysoohigh.service.ImportDataService;
import org.springframework.stereotype.Service;

@Service
public class BuyService implements IBuyService {
    private ImportDataService dataService;

    public BuyService(ImportDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public String buy(String itemToBuy, String currentUser) {
        if (currentUser.isEmpty()) {
            return "You are not logged in";
        }
        if (!dataService.isItemInShopList(itemToBuy)) {
            return "This item is not in the shop list";
        }
        int customerFunds = dataService.getCustomerFunds(currentUser);
        int itemPrice = dataService.getItemPrice(itemToBuy);
        if (itemPrice > customerFunds) {
            return "Sorry, not enough funds to purchase this item";
        }
        dataService.buyItem(currentUser, itemToBuy, itemPrice);
        return "Purchase is successful!";
    }
}
