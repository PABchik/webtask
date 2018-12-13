package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.Organisation;
import com.Paul.web.app.entity.Role;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.repository.OrganisationRepository;
import com.Paul.web.app.repository.RoleRepository;
import com.Paul.web.app.repository.UserRepository;
import com.Paul.web.app.service.OrganisationService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Service
@Transactional(Transactional.TxType.REQUIRES_NEW) /*search*/
public class OrganisationServiceImpl implements OrganisationService {
    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

//    @Autowired
//    GroupRepository groupRepository;

//    @Autowired
//    GroupService groupService;

    @Override
    public Organisation createOrganisation(User owner, Organisation newOrganisation) {
        if (organisationRepository.findByOwnerId(owner.getId()) == null &&
                organisationRepository.findByName(newOrganisation.getName()) == null &&
                owner.getOrganisation() == null) {
            newOrganisation.setOwnerId(owner.getId());
            Set<User> organisationParticipants = newOrganisation.getParticipants();
            organisationParticipants.add(owner);
            newOrganisation = organisationRepository.save(newOrganisation);

            owner.setOrganisation(newOrganisation);
            owner.getUserRoles().add(roleRepository.findByName("ORGANISATION_OWNER"));
            userService.saveUser(owner);
        }



        return newOrganisation;
    }

    @Override
    public User addParticipant(User newParticipant, Organisation organisation) {

        if (newParticipant.getOrganisation() == null) {
            Set<User> participants = organisation.getParticipants();
            participants.add(newParticipant);
            organisation.setParticipants(participants);
            organisationRepository.save(organisation);
            newParticipant.setOrganisation(organisation);
            return userRepository.save(newParticipant);
        }

        return null;

    }

    @Override
    public Organisation findByName(String name) {
        return organisationRepository.findByName(name);
    }

    @Override
    public void deleteParticipant(User user) {
        Organisation organisation = user.getOrganisation();
        Set<User> participants = organisation.getParticipants();
        participants.remove(user);
        organisation.setParticipants(participants);
        organisationRepository.save(organisation);
        user.setUserRoles(new HashSet<Role>(Arrays.asList(roleRepository.findByName("USER"))));
//        user.setGroup(null);
        userService.saveUser(user);
    }

    /*@Override
    public void addGroupAdmin(User user) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getOrganisation() == user.getOrganisation()) {
            userService.addRole("GROUP_ADMIN", user);
        }
    }

    @Override
    public void addTestManager(User user) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getOrganisation() == user.getOrganisation()) {
            userService.addRole("TEST_MANAGER", user);
        }
    }

    @Override
    public void addStudent(User user) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getOrganisation() == user.getOrganisation()) {
            userService.addRole("STUDENT", user);
        }
    }

    @Override
    public void changeOrganisationOwner(User user) {
        User currentOwner = userService.getCurrentUser();
        if (user.getOrganisation() == currentOwner.getOrganisation()) {
            userService.deleteRole("ORGANISATION_OWNER", currentOwner);
            Organisation organisation = currentOwner.getOrganisation();
            organisation.setOwnerId(user.getId());
            organisationRepository.save(organisation);
            userService.addRole("ORGANISATION_OWNER", user);
        }
    }*/

    @Override
    public void deleteOrganisation(@RequestHeader("jwt_token") String token) {
        User organisationOwner = userService.getCurrentUser(token);
        Organisation organisation = organisationOwner.getOrganisation();
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleRepository.findByName("USER"));
        for (User participant : organisation.getParticipants()) {
            participant.setOrganisation(null);
            participant.setUserRoles(roles);
//            participant.setGroup(null);
            userService.saveUser(participant);
        }

        organisationRepository.delete(organisation);
    }

    @Override
    public Organisation findById(int organisation) {
        return organisationRepository.findById(organisation);
    }

    @Override
    public Set<Organisation> findAll() {
        return new HashSet<>(organisationRepository.findAll());
    }

    @Override
    public Organisation save(Organisation organisation) {
        return organisationRepository.save(organisation);
    }


    /*@Override
    public void removeGroupAdmin(User user) {
        userService.deleteRole("GROUP_ADMIN", user);
        Organisation organisation = user.getOrganisation();
//        User organisationOwner = userRepository.findById(organisation.getOwnerId());
        for (Group group : groupRepository.findAllAdminsGroups(user.getId())) {

            groupService.setGroupAdminId(group, organisation.getOwnerId());
        }
    }*/

    /*@Override
    public void removeTestManager(User user) {

    }

    @Override
    public void removeStudent(User user) {

    }*/
}
