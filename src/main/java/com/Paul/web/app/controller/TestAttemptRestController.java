package com.Paul.web.app.controller;


import com.Paul.web.app.entity.Test;
import com.Paul.web.app.entity.TestAttempt;
import com.Paul.web.app.service.TestAttemptService;
import com.Paul.web.app.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/test/{testId}/attempts")
public class TestAttemptRestController {

    @Autowired
    TestAttemptService testAttemptService;

    @Autowired
    TestService testService;

    @PostMapping
    public ResponseEntity<TestAttempt> createAttempt(@RequestHeader("jwt_header") String token,
                                                     @PathVariable("testId") int testId) {
        Test test = testService.findById(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        TestAttempt attempt = new TestAttempt();
        attempt.setTest(test);
        attempt = testAttemptService.createAttempt(token, attempt);
        attempt.getTest().setTasks(null);
        attempt.getStudent().setUserRoles(null);
        attempt.getStudent().setPassword(null);
        return ResponseEntity.ok(attempt);
    }

    @GetMapping
    public ResponseEntity<Set<TestAttempt>> showAttempts(@RequestHeader("jwt_header") String token,
                                                         @PathVariable("testId") int testId) {
        Set<TestAttempt> testAttempts = testAttemptService.getTestAttempts(token, testId);
        for (TestAttempt attempt : testAttempts) {
            attempt.getStudent().setPassword(null);
            attempt.getStudent().setUserRoles(null);
        }

        return ResponseEntity.ok(testAttempts);

    }

}
