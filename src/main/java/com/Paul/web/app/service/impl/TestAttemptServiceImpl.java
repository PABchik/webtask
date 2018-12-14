package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.TestAttempt;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.repository.TestAttemptRepository;
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

    @Transactional
    @Override
    public TestAttempt createAttempt(String token, TestAttempt attempt) {

        User user = userService.getCurrentUser(token);
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
    public Set<TestAttempt> getTestAttempts(String token, int testId) {
        User user = userService.getCurrentUser(token);
        return testAttemptRepository.getStudentTestAttempts(user.getId(), testId);
    }

    @Override
    public TestAttempt findById(int attemptId) {

        return testAttemptRepository.findById(attemptId);
    }
}
