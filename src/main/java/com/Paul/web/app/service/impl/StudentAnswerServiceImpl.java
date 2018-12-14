package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.AnswerOption;
import com.Paul.web.app.entity.StudentAnswer;
import com.Paul.web.app.entity.TestAttempt;
import com.Paul.web.app.repository.StudentAnswerRepository;
import com.Paul.web.app.service.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentAnswerServiceImpl implements StudentAnswerService {

    @Autowired
    StudentAnswerRepository studentAnswerRepository;

    @Override
    public StudentAnswer createStudentAnswer(AnswerOption ansOpt, TestAttempt attempt) {
        StudentAnswer answer = new StudentAnswer();
        answer.setAttempt(attempt);
        answer.setAnswerOption(ansOpt);
        answer.setCorrect(ansOpt.isCorrect());
        return studentAnswerRepository.save(answer);
    }
}
