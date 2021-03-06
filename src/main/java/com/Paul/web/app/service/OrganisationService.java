package com.Paul.web.app.service;

import com.Paul.web.app.entity.Organisation;
import com.Paul.web.app.entity.User;

import java.util.Set;

public interface OrganisationService {

    public User addParticipant(User newParticipant, Organisation organisation);
    public Organisation createOrganisation(User owner, Organisation newOrganisation);

    Organisation findByName(String name);

    void deleteParticipant(User user);

    /*void addGroupAdmin(User user);

    void addTestManager(User user);

    void addStudent(User user);

    void changeOrganisationOwner(User user);
*/
    void deleteOrganisation();

    Organisation findById(int organisation);

    Set<Organisation> findAll();

    Organisation save(Organisation organisation);

    User deleteGroupAdmin(User user);

    User deleteTestManager(User user);

    User deleteStudent(User user);

    /*void removeGroupAdmin(User user);

    void removeTestManager(User user);

    void removeStudent(User user);*/

}
