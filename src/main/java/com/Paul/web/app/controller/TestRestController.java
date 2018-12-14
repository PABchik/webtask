package com.Paul.web.app.controller;

import com.Paul.web.app.entity.Test;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "api/organisation/tests")
public class TestRestController {

    @Autowired
    TestService testService;


//    @PreAuthorize("isTestManager() || isOrganisationOwner()")
    @GetMapping
    public ResponseEntity<Set<Test>> showTests(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(testService.findAllOrgTests(token));
    }

//    @PreAuthorize("isTestManager()")
    @PostMapping
    public ResponseEntity<Test> createTest(@RequestHeader("jwt_header") String token, @RequestBody Test test) {
        if (test == null) {
            throw new BuisnessException("incorrect test");
        }
        testService.createTest(test, token);
        return ResponseEntity.ok(test);
    }

//    @PreAuthorize("isTestManager() || isOrganisationOwner()")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Test> deleteTest(@RequestHeader("jwt_header") String token, @PathVariable int testId) {
        if (testService.findById(testId) == null) {
            ResponseEntity.notFound().build();
        }
        testService.deleteTest(testService.findById(testId), token);
        return ResponseEntity.ok().build();
    }

}
