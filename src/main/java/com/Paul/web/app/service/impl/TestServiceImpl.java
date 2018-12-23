package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.Group;
import com.Paul.web.app.entity.Test;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.repository.TestRepository;
import com.Paul.web.app.service.GroupService;
import com.Paul.web.app.service.TestService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    TestRepository testRepository;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Override
    public Test createTest(Test test) {
        if (testRepository.findByName(test.getName()) != null) {
            throw new BuisnessException("This test already exists");
        }
        test.setOrganisationId(userService.getCurrentUser().getOrganisation().getId());
        test.setManagerId(userService.getCurrentUser().getId());
        return testRepository.save(test);
    }

    @Override
    public Test findById(int testId) {
        return testRepository.findById(testId);
    }

    @Override
    public void deleteTest(Test test) {
        User user = userService.getCurrentUser();
        if (test.getManagerId() != user.getId() &&
                user.getOrganisation().getOwnerId() != user.getId()) {
            throw new BuisnessException("You have not enough access permissions");
        }

        testRepository.delete(test);

    }

    @Override
    public Set<Test> findAllOrgTests(String token) {

        return testRepository.findAllOrgTests(userService.getCurrentUser().getOrganisation().getId());
    }

    @Override
    public Test assignTest(Group group, Test test) {
        Set<Test> groupTests = new HashSet<>();
        groupTests.add(test);
        group.setTests(groupTests);
        groupService.saveGroup(group);
        return test;

    }
}

