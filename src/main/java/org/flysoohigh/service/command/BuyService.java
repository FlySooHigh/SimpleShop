package org.flysoohigh.service.command;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

//@Service
public class BuyService implements ICommandService {
    private PrintWriter out;
    private ImportDataService dataService;

    public BuyService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public void handleInput(String[] parsedCommand, String currentUser) {
        if (currentUser.isEmpty()) {
            out.println("You are not logged in");
            return;
        }
        if (parsedCommand.length < 2) {
            out.println("You have to specify item name that you want to buy");
            return;
        }
        String itemName = parsedCommand[1];
        if (!dataService.isItemInShopList(itemName)) {
            out.println("This item is not in the shop list");
            return;
        }
        int customerFunds = dataService.getCustomerFunds(currentUser);
        int itemPrice = dataService.getItemPrice(itemName);
        if (itemPrice > customerFunds) {
            out.println("Sorry, not enough funds to purchase this item");
            return;
        }
        dataService.buyItem(currentUser, itemName, itemPrice);
        out.println("Purchase is successful!");
    }
}
