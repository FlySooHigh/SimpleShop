package org.flysoohigh.service.logout;

import org.flysoohigh.util.Pair;

public interface ILogoutService {
    Pair<String, String> logout(String currentUser);
}
