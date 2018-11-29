package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.security.UserDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/login")
    public User getDetails() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getUser();
    }

}
