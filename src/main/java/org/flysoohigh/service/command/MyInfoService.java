package org.flysoohigh.service.command;

import org.flysoohigh.model.Customer;
import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class MyInfoService implements CommandService {

    private PrintWriter out;
    private ImportDataService dataService;

    public MyInfoService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public void handleInput(String[] parsedCommand, String loggedInCustomer) {
            if (loggedInCustomer.isEmpty()) {
                out.println("You are not logged in");
            } else {
                Customer customerInfo = dataService.getInfo(loggedInCustomer);
                out.println(customerInfo.toString());
            }
    }
}
