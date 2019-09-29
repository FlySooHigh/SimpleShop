package org.flysoohigh.service.logout;

import org.flysoohigh.service.ImportDataService;
import org.flysoohigh.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements ILogoutService {
    private static final String EMPTY_STRING = "";

    private ImportDataService dataService;

    public LogoutService(ImportDataService dataService) {
        this.dataService = dataService;
    }

    public Pair<String, String> logout(String currentUser) {
        if (currentUser.isEmpty()) {
            return new Pair<>(currentUser, "You are not logged in");
        }
        dataService.logOut(currentUser);
        return new Pair<>(EMPTY_STRING, "Logout successful");
    }
}
