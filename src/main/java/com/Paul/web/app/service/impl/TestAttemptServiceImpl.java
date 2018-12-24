package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.TestAttempt;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.repository.TestAttemptRepository;
import com.Paul.web.app.repository.TestRepository;
import com.Paul.web.app.service.TestAttemptService;
import com.Paul.web.app.service.TestService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class TestAttemptServiceImpl implements TestAttemptService {

    @Autowired
    UserService userService;

    @Autowired
    TestService testService;

    @Autowired
    TestAttemptRepository testAttemptRepository;

    @Autowired
    TestRepository testRepository;

    @Transactional
    @Override
    public TestAttempt createAttempt(TestAttempt attempt) {

        User user = userService.getCurrentUser();
        attempt.setStudent(user);

        int attemptsNumber = testAttemptRepository.getAttemptNumber(attempt.getTest().getId(),
                attempt.getStudent().getId()) + 1;

        if (attemptsNumber > attempt.getTest().getMaxAttempts()) {
            throw new BuisnessException("Limit of attempts is exhausted");
        }

        attempt.setAttemptNumber(attemptsNumber);
        return testAttemptRepository.save(attempt);
    }

    @Override
    public Set<TestAttempt> getTestAttempts(int testId) {
        User user = userService.getCurrentUser();
        return testAttemptRepository.getStudentTestAttempts(user.getId(), testId);
    }

    @Override
    public TestAttempt findById(int attemptId) {

        return testAttemptRepository.findById(attemptId);
    }

    @Override
    public Set<TestAttempt> getTestManagerTestAttempts(int testId) {
        return testAttemptRepository.findByTestManagerId(testRepository.findById(testId).getManagerId().getId());
    }
}
