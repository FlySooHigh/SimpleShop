package org.flysoohigh.service.login;

import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

//@Service
public class LogoutService implements ILoginService {

    private PrintWriter out;
    private ImportDataService dataService;

    public LogoutService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public String handleInput(String[] parsedCommand, String currentUser) {
        if (currentUser.isEmpty()) {
            out.println("You are not logged in");
            return currentUser;
        }
        dataService.logOut(currentUser);
        out.println("Logout successful");
        return "";
    }
}
