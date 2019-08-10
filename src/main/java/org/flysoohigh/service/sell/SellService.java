package org.flysoohigh.service.sell;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class SellService implements ISellService {
    private PrintWriter out;
    private ImportDataService dataService;

    public SellService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public void sell(String itemToSell, String currentUser) {
        if (currentUser.isEmpty()) {
            out.println("You are not logged in");
            return;
        }
        if (!dataService.isItemInShopList(itemToSell)) {
            out.println("This item is not in shop list, so you could not buy it");
            return;
        }
        if (!dataService.isInTheCustomerList(currentUser, itemToSell)) {
            out.println("This item is not in your bought items' list");
            return;
        }
        dataService.sellItem(currentUser, itemToSell);
        out.println("Item sold successfully!");
    }
}
