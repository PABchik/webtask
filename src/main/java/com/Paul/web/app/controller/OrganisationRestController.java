package com.Paul.web.app.controller;

import com.Paul.web.app.entity.Organisation;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.service.OrganisationService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/organisation")
public class OrganisationRestController {

    @Autowired
    OrganisationService organisationService;

    @Autowired
    UserService userService;

    @PreAuthorize("isJustUser()")
    @PostMapping("testBack")
    public ResponseEntity<String> testService() {
        return ResponseEntity.ok(new String("ok"));
    }

    @PreAuthorize("isJustUser()")
    @PostMapping
    public Organisation createOrganisation(@RequestBody Organisation organisation) {

        User user = userService.getCurrentUser();
        if (user.getOrganisation() != null) {
            throw new BuisnessException("You are already participant of another organisation");
        }

        organisation = organisationService.createOrganisation(user, organisation);

        for (User i : organisation.getParticipants()) {
            i.setPassword(null);
            i.setOrganisation(null);
        }

        return organisation;
    }


    @PreAuthorize("isOrganisationOwner()")
    @GetMapping
    public ResponseEntity<Organisation> getOrganisation() {
        User user = userService.getCurrentUser();
        if (user.getOrganisation() == null) {
            return ResponseEntity.notFound().build();
        }
        Organisation organisation = user.getOrganisation();
        for (User i : organisation.getParticipants()) {
            i = userService.hideInfo(i);
        }
        return ResponseEntity.ok(organisation);
    }

    @PreAuthorize("isOrganisationOwner()")
    @DeleteMapping
    public ResponseEntity<Organisation> deleteOrganisation() {
        User user = userService.getCurrentUser();
        if (user.getOrganisation() == null) {
            return ResponseEntity.notFound().build();
        }

        organisationService.deleteOrganisation();

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Set<Organisation>> getAllOrganisations() {

        if (organisationService.findAll() == null) {
            return ResponseEntity.notFound().build();
        }

        Set<Organisation> organisations = organisationService.findAll();
        for (Organisation org : organisations) {
            org.setParticipants(null);
            org.setOwnerId(0);
            org.setGroups(null);
        }
        return ResponseEntity.ok(organisations);
    }



}
