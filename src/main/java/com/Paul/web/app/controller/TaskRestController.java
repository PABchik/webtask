package com.Paul.web.app.controller;

import com.Paul.web.app.entity.Task;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.service.TaskService;
import com.Paul.web.app.service.TestService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/organisation/tests/{testId}/tasks")
public class TaskRestController {

    @Autowired
    TaskService taskService;

    @Autowired
    TestService testService;

    @Autowired
    UserService userService;

    @PreAuthorize("isTestManager()")
    @PostMapping
    public ResponseEntity<Task> createTask(@PathVariable("testId") int testId,
                                           @RequestBody Task task) {
        task.setTest(testService.findById(testId));
        if (task == null ||
                task.getTest().getManagerId().getId() != userService.getCurrentUser().getId()) {
            throw new BuisnessException("you cannot manage this test");
        }
        return ResponseEntity.ok(taskService.save(task));
    }

//    @PreAuthorize("isTestManager() || isOrganisationOwner()")
    @GetMapping(value = "/{taskId}")
    public ResponseEntity<Task> showtestTasks(@PathVariable("testId") int testId,
                                              @PathVariable("taskId") int taskId) {
        Task task = taskService.findById(taskId);
        if (task == null || task.getTest().getId() != testId ||
                task.getTest().getManagerId().getId() != userService.getCurrentUser().getId()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

//    @PreAuthorize("isTestManager()")
    @DeleteMapping(value = "/{taskId}")
    public ResponseEntity<Task> deleteTask(@PathVariable("testId") int testId,
                                           @PathVariable("taskId") int taskId) {

        Task task = taskService.findById(taskId);
        if (task == null || task.getTest().getId() != testId ||
                task.getTest().getManagerId().getId() != userService.getCurrentUser().getId()) {
            return ResponseEntity.notFound().build();
        }
        taskService.deleteTask(task);
        return ResponseEntity.ok().build();
    }
}
