package com.Paul.web.app.service;

import com.Paul.web.app.entity.User;

public interface UserService {

    User findUserByEmail(String email);

    User registerNewUser(User newUser);
}
