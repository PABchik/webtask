package com.Paul.web.app.service;

import com.Paul.web.app.entity.User;

public interface UserService {

    User saveUser(User user);

    User findUserByEmail(String email);

    User registerNewUser(User newUser);

    User getCurrentUser(String token);
}
