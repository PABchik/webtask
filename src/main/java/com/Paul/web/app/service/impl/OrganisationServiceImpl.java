package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.*;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.repository.*;
import com.Paul.web.app.service.OrganisationService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private GroupRepository groupRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    RoleRepository roleRepository;

//    @Autowired
//    GroupRepository groupRepository;

//    @Autowired
//    GroupService groupService;

    @Override
    public Organisation createOrganisation(User owner, Organisation newOrganisation) {
        if (organisationRepository.findByOwnerId(owner.getId()) != null ||
                organisationRepository.findByName(newOrganisation.getName()) != null ||
                owner.getOrganisation() != null) {
            throw new BuisnessException("Error by creating new organisation");
        }
            newOrganisation.setOwnerId(owner.getId());
            Set<User> organisationParticipants = newOrganisation.getParticipants();
            organisationParticipants.add(owner);
            newOrganisation = organisationRepository.save(newOrganisation);

            owner.setOrganisation(newOrganisation);
            owner.getUserRoles().add(roleRepository.findByName("ORGANISATION_OWNER"));
            owner.getUserRoles().add(roleRepository.findByName("TEST_MANAGER"));
            owner.getUserRoles().add(roleRepository.findByName("GROUP_ADMIN"));
            userService.saveUser(owner);

        return newOrganisation;
    }


    @Transactional
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
        /*Organisation organisation = user.getOrganisation();
        Set<User> participants = organisation.getParticipants();
        participants.remove(user);
        organisation.setParticipants(participants);
        organisationRepository.save(organisation);*/
        user.setUserRoles(new HashSet<Role>(Arrays.asList(roleRepository.findByName("USER"))));
        user.setOrganisation(null);
        user.setGroup(null);
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
    public void deleteOrganisation() {
        User organisationOwner = userService.getCurrentUser();
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
//        User organisationOwner = userRepository.findById(organ1isation.getOwnerId());
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

    @Override
    public User deleteGroupAdmin(User user) {
        Set<Group> groups = groupRepository.findGroupsByAdminId(user.getId());
        if (groups != null) {
            if (!userService.
                    getCurrentUser().
                    getUserRoles().
                    contains(roleRepository.
                    findByName("GROUP_ADMIN"))) {
                User owner = userService.getCurrentUser();
                owner.getUserRoles().add(roleRepository.findByName("GROUP_ADMIN"));
                userService.saveUser(owner);
            }
            for (Group group : groups) {
                group.setGroupAdminId(userService.getCurrentUser().getId());
                groupRepository.save(group);
            }
        }
        user.getUserRoles().remove(roleRepository.findByName("GROUP_ADMIN"));
        userService.saveUser(user);
        return user;
    }

    @Override
    public User deleteTestManager(User user) {
        Set<Test> tests = testRepository.findByTestManagerId(user.getId());
        if (tests != null) {
            if (userService.getCurrentUser().getUserRoles().contains(roleRepository.
                    findByName("TEST_MANAGER"))) {
                User owner = userService.getCurrentUser();
                owner.getUserRoles().add(roleRepository.findByName("TEST_MANAGER"));
                userService.saveUser(owner);
            }
            for (Test test : tests) {
                test.setManagerId(userService.getCurrentUser());
                testRepository.save(test);
            }
            user.getUserRoles().remove(roleRepository.findByName("TEST_MANAGER"));
            userService.saveUser(user);
        }
        return user;
    }

    @Override
    public User deleteStudent(User user) {
        user.setGroup(null);
        user.getUserRoles().remove(roleRepository.findByName("STUDENT"));
        userService.saveUser(user);
        return user;
    }
}
