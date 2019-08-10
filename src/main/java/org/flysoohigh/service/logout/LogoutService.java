package org.flysoohigh.service.logout;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class LogoutService implements ILogoutService {

    private static final String EMPTY_STRING = "";

    private PrintWriter out;
    private ImportDataService dataService;

    public LogoutService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    public String logout(String currentUser) {
        if (currentUser.isEmpty()) {
            out.println("You are not logged in");
            return currentUser;
        }
        dataService.logOut(currentUser);
        out.println("Logout successful");
        return EMPTY_STRING;
    }
}
