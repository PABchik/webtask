package com.Paul.web.app.service;

import com.Paul.web.app.entity.AnswerOption;
import com.Paul.web.app.entity.StudentAnswer;
import com.Paul.web.app.entity.TestAttempt;

public interface StudentAnswerService {


    StudentAnswer createStudentAnswer(AnswerOption ansOpt, TestAttempt attempt);
}
