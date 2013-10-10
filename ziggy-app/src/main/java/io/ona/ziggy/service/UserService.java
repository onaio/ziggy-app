package org.ei.ziggy.service;

import org.ei.ziggy.repository.AllSettings;
import org.ei.ziggy.util.Session;

public class UserService {
    private final AllSettings allSettings;
    private final Session session;

    public UserService(AllSettings allSettings, Session session) {
        this.allSettings = allSettings;
        this.session = session;
    }

    public void initUser(String username, String password) {
        session.setPassword(password);
        allSettings.registerReporter(username, password);
    }
}
