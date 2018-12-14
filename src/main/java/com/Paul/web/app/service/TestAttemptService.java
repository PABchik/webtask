package com.Paul.web.app.service;

import com.Paul.web.app.entity.TestAttempt;

import java.util.Set;

public interface TestAttemptService {

    TestAttempt createAttempt(String token, TestAttempt attempt);

    Set<TestAttempt> getTestAttempts(String token, int testId);

    TestAttempt findById(int attemptId);
}
