package com.Paul.web.app.controller;

import com.Paul.web.app.entity.User;
import com.Paul.web.app.security.UserDetails;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> registryUser(@RequestBody User user) {

        if (userService.findUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().build();
        }
        user = userService.registerNewUser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/login")
    public User getDetails() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getUser();
    }



}
