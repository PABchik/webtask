package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.User;
import com.Paul.web.app.repository.UserRepository;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    public User findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

}
