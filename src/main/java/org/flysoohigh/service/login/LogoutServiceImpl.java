package org.flysoohigh.service.login;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class LogoutServiceImpl implements LoginService {

    private PrintWriter out;
    private ImportDataService dataService;

    public LogoutServiceImpl(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public String handleInput(String[] parsedCommand, String loggedInCustomer) {
            if (loggedInCustomer.isEmpty()) {
                out.println("You are not logged in");
            } else {
                dataService.logOut(loggedInCustomer);
                loggedInCustomer = "";
                out.println("Logout successful");
            }
        return loggedInCustomer;
    }
}
