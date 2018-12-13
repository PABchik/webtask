package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.Role;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.repository.RoleRepository;
import com.Paul.web.app.repository.UserRepository;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RoleRepository roleRepository;

    public User findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }


    @Override
    public User registerNewUser(User newUser) {
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        newUser.setUserRoles(new HashSet<Role>(
                Arrays.asList(roleRepository.findByName("USER"))));
        return userRepository.save(newUser);
    }
}
