package com.Paul.web.app.controller;

import com.Paul.web.app.entity.AnswerOption;
import com.Paul.web.app.entity.Task;
import com.Paul.web.app.service.AnswerOptionService;
import com.Paul.web.app.service.TaskService;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/organisation/tests/{testId}/tasks/{taskId}/answerOptions")
public class AnswerOptionRestController {

    @Autowired
    TaskService taskService;

    @Autowired
    AnswerOptionService answerOptionService;

    @Autowired
    UserService userService;

    @PreAuthorize("isTestManager()")
    @PostMapping
    public ResponseEntity<AnswerOption> createAnswerOption(@PathVariable("testId") int testId,
                                                           @PathVariable("taskId") int taskId,
                                                           @RequestBody AnswerOption ansOpt) {
        Task task = taskService.findById(taskId);
        if (task == null ||
                task.getTest().getId() != testId || ansOpt == null ||
                task.getTest().getManagerId().getId() != userService.getCurrentUser().getId()) {
            return ResponseEntity.notFound().build();
        }
        ansOpt.setTask(task);
        return ResponseEntity.ok(answerOptionService.saveAnswerOption(ansOpt));
    }

    @PreAuthorize("isOrganisationOwner() || isTestManager() || isStudent()")
    @GetMapping(value = "/{ansOptId}")
    public ResponseEntity<AnswerOption> showAnswerOption(@PathVariable("testId") int testId,
                                                         @PathVariable("taskId") int taskId,
                                                         @PathVariable("ansOptId") int ansOptId) {
        Task task = taskService.findById(taskId);
        if (task == null ||
                task.getTest().getId() != testId ||
                !task.getAnswerOptions().contains(answerOptionService.findById(ansOptId))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answerOptionService.findById(ansOptId));
    }
}

