package org.flysoohigh.service.command;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class BuyService implements CommandService {
    private PrintWriter out;
    private ImportDataService dataService;

    public BuyService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public void handleInput(String[] parsedCommand, String loggedInCustomer) {
            if (loggedInCustomer.isEmpty()) {
                out.println("You are not logged in");
            } else {
                if (parsedCommand.length < 2) {
                    out.println("You have to specify item name that you want to buy");
                } else {
                    String itemName = parsedCommand[1];
                    if (dataService.isInTheShopList(itemName)) {
                        int customerFunds = dataService.getCustomerFunds(loggedInCustomer);
                        int itemPrice = dataService.getItemPrice(itemName);
                        if (customerFunds >= itemPrice) {
                            dataService.buyItem(loggedInCustomer, itemName, itemPrice);
                            out.println("Purchase is successful!");
                        } else {
                            out.println("Sorry, not enough funds to purchase this item");
                        }
                    } else {
                        out.println("This item is not in the shop list");
                    }
                }
            }
    }
}
