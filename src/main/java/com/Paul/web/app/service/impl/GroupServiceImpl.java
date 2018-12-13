package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.Group;
import com.Paul.web.app.entity.Organisation;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.repository.GroupRepository;
import com.Paul.web.app.service.GroupService;
import com.Paul.web.app.service.OrganisationService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GroupServiceImpl implements GroupService {


    @Autowired
    GroupRepository groupRepository;

    @Autowired
    OrganisationService organisationService;

    @Autowired
    UserService userService;

    @Override
    public Group createGroup(Group newGroup, User currentAdmin) {

        if (groupRepository.findByNumber(newGroup.getNumber()) != null) {
            throw new BuisnessException("Group already exists");
        }

        newGroup.setGroupAdminId(currentAdmin.getId());
        newGroup.setOrganisation(currentAdmin.getOrganisation());
        return groupRepository.save(newGroup);
    }

    @Override
    public void setGroupAdminId(Group group, int id) {
        group.setGroupAdminId(id);
        groupRepository.save(group);
    }

    @Override
    public void test(Set<Group> group) {
        for (Group gr : group) {
            gr.setNumber(gr.getNumber() + "ttt");
            groupRepository.save(gr);
        }
    }

    @Override
    public Set<Group> findByAdmin(int userId) {
        return groupRepository.findGroupsByAdminId(userId);
    }

    @Override
    public void deleteGroup(int groupId, String token) {
        Group group = groupRepository.findById(groupId);

        if (group == null || group.getGroupAdminId() != userService.getCurrentUser(token).getId() &&
                userService.getCurrentUser(token).getId() != group.getOrganisation().getOwnerId()) {
            return;
        }
        Organisation organisation = group.getOrganisation();
        organisation.getGroups().remove(group);
        organisationService.save(organisation);
        group.setOrganisation(null);
        for (User user : group.getParticipants()) {
            user.setGroup(null);
            userService.saveUser(user);
        }
        groupRepository.delete(group);

    }

    @Override
    public Group findById(int groupId) {
        return groupRepository.findById(groupId);
    }


}
