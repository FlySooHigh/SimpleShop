package org.flysoohigh.service.command;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class SellService implements CommandService {
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
            } else {
                if (parsedCommand.length < 2) {
                    out.println("You have to specify item name that you want to sell");
                } else {
                    String itemName = parsedCommand[1];
                    if (dataService.isInTheShopList(itemName)) {
                        if (dataService.isInTheCustomerList(loggedInCustomer, itemName)) {
                            dataService.sellItem(loggedInCustomer, itemName);
                            out.println("Item sold successfully!");
                        } else {
                            out.println("This item is not in your bought items' list");
                        }
                    } else {
                        out.println("This item is not in your bought items' list");
                    }
                }
            }
    }
}
