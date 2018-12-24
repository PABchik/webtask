package com.Paul.web.app.controller;

import com.Paul.web.app.entity.Organisation;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.security.UserDetails;
import com.Paul.web.app.service.OrganisationService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController {

    @Autowired
    UserService userService;

    @Autowired
    OrganisationService organisationService;

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

    @PreAuthorize("isJustUser()")
    @PatchMapping(value = "api/user/{id}")
    public ResponseEntity<User> joinOrganisation(@PathVariable int id) {

        User user = userService.getCurrentUser();
        Organisation organisation = organisationService.findById(id);
        if (organisation == null || user == null) {
            ResponseEntity.notFound();
        }
        user = organisationService.addParticipant(user, organisation);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("isJustUser()")
    @PatchMapping(value = "api/user/left-org")
    public ResponseEntity<User> leftOrganisation() {

        User user = userService.getCurrentUser();
        if (user.getOrganisation() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.leftOrganisation(user));
    }

    @GetMapping(value = "api/user")
    public ResponseEntity<User> getUserInfo() {
        User user = userService.getCurrentUser();
        if (user.getOrganisation() != null) {
            user.getOrganisation().setParticipants(null);
            user.getOrganisation().setGroups(null);
        }
        user.setPassword("");
        user.setTestAttempts(null);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "api/user/organisation")
    public ResponseEntity<Organisation> getUserOrganisation() {
        Organisation organisation = userService.getCurrentUser().getOrganisation();
        return ResponseEntity.ok(organisation);
    }
}
