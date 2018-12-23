package com.Paul.web.app.controller;

import com.Paul.web.app.entity.Group;
import com.Paul.web.app.entity.Test;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.service.GroupService;
import com.Paul.web.app.service.TestService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "api/organisation/tests")
public class TestRestController {

    @Autowired
    TestService testService;

    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

//    @PreAuthorize("isTestManager() || isOrganisationOwner()")
    @GetMapping
    public ResponseEntity<Set<Test>> showTests(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(testService.findAllOrgTests(token));
    }

//    @PreAuthorize("isTestManager()")
    @PostMapping
    public ResponseEntity<Test> createTest(@RequestBody Test test) {
        if (test == null) {
            throw new BuisnessException("incorrect test");
        }
        testService.createTest(test);
        return ResponseEntity.ok(test);
    }

//    @PreAuthorize("isTestManager() || isOrganisationOwner()")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Test> deleteTest(@PathVariable int testId) {
        if (testService.findById(testId) == null) {
            ResponseEntity.notFound().build();
        }
        testService.deleteTest(testService.findById(testId));
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "{testId}/{groupId}")
    public ResponseEntity<Test> assignTest(@PathVariable("testId") int testId,
                                           @PathVariable("groupId") int groupId) {
        Group group = groupService.findById(groupId);
        Test test = testService.findById(testId);

        if (test == null ||
                test.getManagerId() != userService.getCurrentUser().getId() ||
                group == null) {
            return ResponseEntity.notFound().build();
        }

        test = testService.assignTest(group, test);
        return ResponseEntity.ok(test);
    }


}
