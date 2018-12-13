package com.Paul.web.app.service;

import com.Paul.web.app.entity.User;

import java.util.Set;

public interface UserService {

    User saveUser(User user);

    User findUserByEmail(String email);

    User registerNewUser(User newUser);

    User getCurrentUser(String token);

    User getUserFromSecurityContext();

    Set<User> findByOrg(String token);

    User findUserById(int userId);

    User assignRoles(User userChanged, String token);
}
