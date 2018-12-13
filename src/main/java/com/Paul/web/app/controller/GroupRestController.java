package com.Paul.web.app.controller;

import com.Paul.web.app.entity.Group;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.service.GroupService;
import com.Paul.web.app.service.OrganisationService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

//    @PreAuthorize("!isJustUser()") /*only 4 grAdmin or TM*/
    @GetMapping
    public ResponseEntity<Set<Group>> showGroups(@RequestHeader("jwt_header") String token) {
        if (userService.getCurrentUser(token) == null ||
                userService.getCurrentUser(token).getOrganisation() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.getCurrentUser(token).getOrganisation().getGroups());
    }

//    @PreAuthorize("isGroupAdmin()")
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestHeader("jwt_header") String token, @RequestBody Group group) {

        if (group == null) {
            return ResponseEntity.badRequest().build();
        }
        if (userService.getCurrentUser(token).getOrganisation().getOwnerId() != userService.getCurrentUser(token).getId() &&
                groupService.findByAdmin(userService.getCurrentUser(token).getId()) == null) {
            throw new BuisnessException("You aren't group admin or organisation owner, access denied");
        }
        return ResponseEntity.ok(groupService.createGroup(group, userService.getCurrentUser(token)));
    }

//    @PreAuthorize("isOrganisationOwner() || isGroupAdmin()")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteGroup(@RequestHeader("jwt_header") String token, @PathVariable("id") int groupId) {

        Group group = groupService.findById(groupId);
        User user = userService.getCurrentUser(token);
        if (group == null ||
                user.getId() != group.getGroupAdminId() &&
                        user.getId() != group.getOrganisation().getOwnerId()) {
            return ResponseEntity.badRequest().build();
        }
        groupService.deleteGroup(groupId, token);
        return ResponseEntity.ok().build();
    }

//    @PreAuthorize("isOrganisationOwner() || isGroupAdmin() || isTestManager()")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Group> showGroup(@RequestHeader("jwt_header") String token,
                                           @PathVariable("id") int groupId) {
        Group group = groupService.findById(groupId);
        if (group == null ||
                !group.getOrganisation().getParticipants().contains(userService.getCurrentUser(token))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

}
