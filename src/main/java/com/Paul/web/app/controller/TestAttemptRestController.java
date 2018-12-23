package com.Paul.web.app.controller;


import com.Paul.web.app.entity.Test;
import com.Paul.web.app.entity.TestAttempt;
import com.Paul.web.app.service.TestAttemptService;
import com.Paul.web.app.service.TestService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/test/{testId}/attempts")
public class TestAttemptRestController {

    @Autowired
    TestAttemptService testAttemptService;

    @Autowired
    TestService testService;

    @Autowired
    UserService userService;

    @PreAuthorize("isStudent()")
    @PostMapping
    public ResponseEntity<TestAttempt> createAttempt(@PathVariable("testId") int testId) {
        Test test = testService.findById(testId);
        if (test == null ||
                !userService.getCurrentUser().getGroup().getTests().contains(test)) {
            return ResponseEntity.notFound().build();
        }
        TestAttempt attempt = new TestAttempt();
        attempt.setTest(test);
        attempt = testAttemptService.createAttempt(attempt);
        attempt.getTest().setTasks(null);
        attempt.getStudent().setUserRoles(null);
        attempt.getStudent().setPassword(null);
        return ResponseEntity.ok(attempt);
    }

    @GetMapping
    public ResponseEntity<Set<TestAttempt>> showAttempts(@PathVariable("testId") int testId) {
        Set<TestAttempt> testAttempts = testAttemptService.getTestAttempts(testId);
        for (TestAttempt attempt : testAttempts) {
            attempt.getStudent().setPassword(null);
            attempt.getStudent().setUserRoles(null);
        }

        return ResponseEntity.ok(testAttempts);
    }

}
