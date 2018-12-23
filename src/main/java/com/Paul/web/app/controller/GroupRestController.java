package com.Paul.web.app.controller;

import com.Paul.web.app.entity.Group;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.service.GroupService;
import com.Paul.web.app.service.OrganisationService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "api/organisation/groups")
public class GroupRestController {

    @Autowired
    UserService userService;

    @Autowired
    OrganisationService organisationService;

    @Autowired
    GroupService groupService;

    @PreAuthorize("isGroupAdmin() || isTestManager()") /*only 4 grAdmin or TM*/
    @GetMapping
    public ResponseEntity<Set<Group>> showGroups() {
        if (userService.getCurrentUser() == null ||
                userService.getCurrentUser().getOrganisation() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.getCurrentUser().getOrganisation().getGroups());
    }

    @PreAuthorize("isGroupAdmin()")
    @GetMapping(value = "/my")
    public ResponseEntity<Set<Group>> showOwnedGroups() {
        if (userService.getCurrentUser() == null ||
                userService.getCurrentUser().getOrganisation() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groupService.getOwnedGroups(userService.getCurrentUser()));
    }


    @PreAuthorize("isGroupAdmin()")
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {

        if (group == null) {
            return ResponseEntity.badRequest().build();
        }
        if (userService.getCurrentUser().getOrganisation().getOwnerId() != userService.getCurrentUser().getId() &&
                groupService.findByAdmin(userService.getCurrentUser().getId()) == null) {
            throw new BuisnessException("You aren't group admin or organisation owner, access denied");
        }
        return ResponseEntity.ok(groupService.createGroup(group, userService.getCurrentUser()));
    }

//    @PreAuthorize("isOrganisationOwner() || isGroupAdmin()")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteGroup(@PathVariable("id") int groupId) {

        Group group = groupService.findById(groupId);
        User user = userService.getCurrentUser();
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        if (user.getId() != group.getGroupAdminId() &&
                user.getId() != group.getOrganisation().getOwnerId()) {
            throw new BuisnessException("you cannot delete this group");
        }
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }

//    @PreAuthorize("isOrganisationOwner() || isGroupAdmin() || isTestManager()")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Group> showGroup(@PathVariable("id") int groupId) {
        Group group = groupService.findById(groupId);
        if (group == null ||
                !group.getOrganisation().getParticipants().contains(userService.getCurrentUser())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

    @PreAuthorize("isGroupAdmin()")
    @PatchMapping("{groupId}/participants/{userId}")
    public ResponseEntity<Group> addGroupParticipant(@PathVariable("userId") int userId,
                                                     @PathVariable("groupId") int groupId) {
        User newParticipant = userService.findUserById(userId);
        Group group = groupService.findById(groupId);

        if (newParticipant == null ||
                group == null) {
            return ResponseEntity.notFound().build();
        }

        if (newParticipant.getGroup() != null) {
            throw new BuisnessException("Student is in another group");
        }

        group = groupService.addParticipant(group, newParticipant);

        return ResponseEntity.ok(group);
    }

    @PreAuthorize("isGroupAdmin()")
    @DeleteMapping(value = "{groupId}/participants/{userId}")
    public void deleteGroupParticipant(@PathVariable("groupId") int groupId,
                                       @PathVariable("userId") int userId) {

        User user = userService.findUserById(userId);
        Group group = groupService.findById(groupId);

        if (user == null ||
                group == null ||
                group.getOrganisation() != userService.getCurrentUser().getOrganisation()) {
            return;
        }
        groupService.deleteParticipant(group, user);
    }

    @PreAuthorize("isGroupAdmin()")
    @PatchMapping(value = "{groupId}")
    public ResponseEntity<Group> renameGroup(@PathVariable("groupId") int groupId,
                                             @RequestBody Group newGroup) {
        Group group = groupService.findById(groupId);
        if (group == null ||
                group.getOrganisation() != userService.getCurrentUser().getOrganisation()) {
            return ResponseEntity.notFound().build();
        }
        group = groupService.renameGroup(group, newGroup.getNumber());
        return ResponseEntity.ok(group);
    }

}
