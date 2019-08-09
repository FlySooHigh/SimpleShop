package org.flysoohigh.service.command;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

//@Service
public class SellService implements ICommandService {
    private PrintWriter out;
    private ImportDataService dataService;

    public SellService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public void handleInput(String[] parsedCommand, String loggedInCustomer) {
        if (loggedInCustomer.isEmpty()) {
            out.println("You are not logged in");
            return;
        }
        if (parsedCommand.length < 2) {
            out.println("You have to specify item name that you want to sell");
            return;
        }
        String itemName = parsedCommand[1];
        if (!dataService.isItemInShopList(itemName)) {
            out.println("This item is not in shop list, so you could not buy it");
            return;
        }
        if (!dataService.isInTheCustomerList(loggedInCustomer, itemName)) {
            out.println("This item is not in your bought items' list");
            return;
        }
        dataService.sellItem(loggedInCustomer, itemName);
        out.println("Item sold successfully!");
    }
}
