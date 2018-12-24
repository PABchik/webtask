package com.Paul.web.app.controller;

import com.Paul.web.app.entity.User;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.repository.RoleRepository;
import com.Paul.web.app.service.OrganisationService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/organisation/participants")
public class ParticipantsRestController {

    @Autowired
    OrganisationService organisationService;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @PreAuthorize("isOrganisationOwner() || isGroupAdmin()")
    @GetMapping
    public ResponseEntity<Set<User>> getParticipants() {
        if (userService.getCurrentUser().getOrganisation() == null) {
            ResponseEntity.notFound().build();
        }
        Set<User> participants = userService.getCurrentUser().getOrganisation().getParticipants();
        for (User user : participants) {
            user.setTestAttempts(null);
            user.setPassword("");
            user.setOrganisation(null);
        }
        return ResponseEntity.ok(participants);
    }

    @PreAuthorize("isOrganisationOwner() || isGroupAdmin()")
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getParticipant(@PathVariable("id") int userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            ResponseEntity.notFound();
        }
        if (!userService.getCurrentUser().getOrganisation().getParticipants().contains(user)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @PreAuthorize("isOrganisationOwner()")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<User> assignRole(@RequestBody User userChanged, @PathVariable("id") int id) {
        userChanged.setId(id);
        userChanged = userService.assignRoles(userChanged);

        userChanged.setPassword("");
        userChanged.setTestAttempts(null);
        userChanged.setGroup(null);
        userChanged.setOrganisation(null);
        return ResponseEntity.ok(userChanged);

    }

    @PreAuthorize("isOrganisationOwner()")
    @DeleteMapping(value = "/groupAdmin/{id}")
    public ResponseEntity<User> deleteGroupAdmin(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
        if (user == null ||
                user.getOrganisation() != userService.getCurrentUser().getOrganisation() ||
                !user.getUserRoles().contains(roleRepository.findByName("GROUP_ADMIN"))) {
            return ResponseEntity.notFound().build();
        }
        user = organisationService.deleteGroupAdmin(user);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("isOrganisationOwner()")
    @DeleteMapping(value = "/testManager/{id}")
    public ResponseEntity<User> deleteTestManager(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
        if (user == null ||
                user.getOrganisation() != userService.getCurrentUser().getOrganisation() ||
                !user.getUserRoles().contains(roleRepository.findByName("TEST_MANAGER"))) {
            return ResponseEntity.notFound().build();
        }
        user = organisationService.deleteTestManager(user);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("isOrganisationOwner()")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteParticipant(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
        if (user == null ||
                user.getOrganisation() != userService.getCurrentUser().getOrganisation()) {
            return ResponseEntity.notFound().build();
        }
        if ( user.getUserRoles().size() != 1) {
            throw new BuisnessException("First of all you should remove all user roles");
        }
        organisationService.deleteParticipant(user);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isOrganisationOwner() || isGroupAdmin()")
    @DeleteMapping(value = "/student/{id}")
    public ResponseEntity<User> deleteStudent(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
        if (user == null ||
                user.getOrganisation() != userService.getCurrentUser().getOrganisation()) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getUserRoles().contains(roleRepository.findByName("STUDENT"))) {
            throw new BuisnessException("Participant doesn't have role STUDENT");
        }
        user = organisationService.deleteStudent(user);
        return ResponseEntity.ok(user);
    }
}
