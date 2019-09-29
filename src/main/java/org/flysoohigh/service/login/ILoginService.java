package org.flysoohigh.service.login;

import org.flysoohigh.util.Pair;

public interface ILoginService {
    Pair<String, String> login(String newUser, String currentUser);
}
