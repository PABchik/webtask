package com.Paul.web.app.service;

import com.Paul.web.app.entity.Group;
import com.Paul.web.app.entity.User;

import java.util.Set;

public interface GroupService {
    public Group createGroup(Group newGroup, User currentAdmin);

    void setGroupAdminId(Group group, int id);

    void test(Set<Group> group);

    Set<Group> findByAdmin(int userId);

    void deleteGroup(int groupId, String token);

    Group findById(int groupId);

    Group addParticipant(Group group, User newParticipant);
}


