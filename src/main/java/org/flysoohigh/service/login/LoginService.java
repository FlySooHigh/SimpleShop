package org.flysoohigh.service.login;

import org.flysoohigh.service.ImportDataService;
import org.flysoohigh.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements ILoginService {
    private ImportDataService dataService;

    public LoginService(ImportDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public Pair<String, String> login(String newUser, String currentUser) {
        if (newUser.isEmpty()) {
            return new Pair<>(currentUser, "You have to specify customer name after login");
        }
        if (!dataService.isCustomer(newUser)) {
            return new Pair<>(currentUser, "Such user is not registered. Enter existing user name.");
        }
        if (!currentUser.isEmpty()) {
            return new Pair<>(currentUser, "Some user is already logged in this terminal");
        }
        if (dataService.isLoggedIn(newUser)) {
            return new Pair<>(currentUser, "You are already logged in another terminal");
        }
        dataService.logIn(newUser);
        return new Pair<>(newUser, "Login successful");
    }
}
