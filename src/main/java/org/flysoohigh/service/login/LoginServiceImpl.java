package org.flysoohigh.service.login;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class LoginServiceImpl implements LoginService {
    private PrintWriter out;
    private ImportDataService dataService;

    public LoginServiceImpl(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public String handleInput(String[] parsedCommand, String currentUser) {
        if (parsedCommand.length < 2) {
            out.println("You have to specify customer name after login");
        } else {
            String newUser = parsedCommand[1];
            if (dataService.isCustomer(newUser)) {
                if (!currentUser.isEmpty()) {
                    out.println("Some user is already logged in this terminal");
                } else if (!dataService.isLoggedIn(newUser)) {
                    dataService.logIn(newUser);
                    out.println("Login successful");
                    return newUser;
                } else {
                    out.println("You are already logged in another terminal");
                }
            } else {
                out.println("Such user is not registered. Enter existing user name.");
            }
        }
        return currentUser;
    }
}
