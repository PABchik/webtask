package com.Paul.web.app.service;

import com.Paul.web.app.entity.AnswerOption;

public interface AnswerOptionService {
    AnswerOption saveAnswerOption(AnswerOption ansOpt);

    AnswerOption findById(int ansOptId);

}
