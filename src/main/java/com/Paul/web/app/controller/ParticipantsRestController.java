package com.Paul.web.app.controller;

import com.Paul.web.app.entity.User;
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

//    @PreAuthorize("isOrganisationOwner() || isGroupAdmin()")
    @GetMapping
    public ResponseEntity<Set<User>> getParticipants(@RequestHeader("jwt_header") String token) {
        if (userService.getCurrentUser(token).getOrganisation() == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.findByOrg(token));
    }

//    @PreAuthorize("isOrganisationOwner() || isGroupAdmin()")
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getParticipant(@RequestHeader("jwt_header") String token,
                                               @PathVariable("id") int userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            ResponseEntity.notFound();
        }
        if (!userService.getCurrentUser(token).getOrganisation().getParticipants().contains(user)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userService.findUserById(userId));
    }

//    @PreAuthorize("isOrganisationOwner()")
    @PutMapping(value = "/{id}")
    public ResponseEntity<User> assignRole(@RequestHeader("jwt_header") String token,
                                           @RequestBody User userChanged, @PathVariable("id") int id) {
        userChanged.setId(id);
        userChanged = userService.assignRoles(userChanged, token);
        return ResponseEntity.ok(userChanged);

    }
}
