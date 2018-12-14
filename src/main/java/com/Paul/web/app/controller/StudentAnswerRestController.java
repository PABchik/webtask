package com.Paul.web.app.controller;


import com.Paul.web.app.entity.AnswerOption;
import com.Paul.web.app.entity.StudentAnswer;
import com.Paul.web.app.entity.TestAttempt;
import com.Paul.web.app.service.AnswerOptionService;
import com.Paul.web.app.service.StudentAnswerService;
import com.Paul.web.app.service.TestAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test/{testId}/attempts/{attemptId}/answers/")
public class StudentAnswerRestController {

    @Autowired
    AnswerOptionService answerOptionService;

    @Autowired
    StudentAnswerService studentAnswerService;

    @Autowired
    TestAttemptService testAttemptService;

    @PostMapping(value = "{answerOptionId}")
    public ResponseEntity<StudentAnswer> createStudentAnswer(@PathVariable("answerOptionId") int ansOptId,
                                                             @PathVariable("attemptId") int attemptId) {

        AnswerOption ansOpt = answerOptionService.findById(ansOptId);
        TestAttempt attempt = testAttemptService.findById(attemptId);
        if (ansOpt == null || attempt == null) {
            return ResponseEntity.notFound().build();
        }
        StudentAnswer answer = studentAnswerService.createStudentAnswer(ansOpt, attempt);
        return ResponseEntity.ok(answer);
    }


}
