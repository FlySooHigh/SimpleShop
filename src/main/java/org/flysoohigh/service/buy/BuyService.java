package org.flysoohigh.service.buy;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class BuyService implements IBuyService {
    private PrintWriter out;
    private ImportDataService dataService;

    public BuyService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public void buy(String itemToBuy, String currentUser) {
        if (currentUser.isEmpty()) {
            out.println("You are not logged in");
            return;
        }
        if (!dataService.isItemInShopList(itemToBuy)) {
            out.println("This item is not in the shop list");
            return;
        }
        int customerFunds = dataService.getCustomerFunds(currentUser);
        int itemPrice = dataService.getItemPrice(itemToBuy);
        if (itemPrice > customerFunds) {
            out.println("Sorry, not enough funds to purchase this item");
            return;
        }
        dataService.buyItem(currentUser, itemToBuy, itemPrice);
        out.println("Purchase is successful!");
    }
}
