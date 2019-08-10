package org.flysoohigh.service.login;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class LoginService implements ILoginService {
    private PrintWriter out;
    private ImportDataService dataService;

    public LoginService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public String login(String newUser, String currentUser) {
        if (newUser.isEmpty()) {
            out.println("You have to specify customer name after login");
            return currentUser;
        }
        if (!dataService.isCustomer(newUser)) {
            out.println("Such user is not registered. Enter existing user name.");
            return currentUser;
        }
        if (!currentUser.isEmpty()) {
            out.println("Some user is already logged in this terminal");
            return currentUser;
        }
        if (dataService.isLoggedIn(newUser)) {
            out.println("You are already logged in another terminal");
            return currentUser;
        }
        dataService.logIn(newUser);
        out.println("Login successful");
        return newUser;
    }
}
