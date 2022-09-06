package com.smartlab.tagman.service;

import com.smartlab.tagman.model.User;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
    User getDetails();
}
