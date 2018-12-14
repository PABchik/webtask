package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.AnswerOption;
import com.Paul.web.app.repository.AnswerOptionRepository;
import com.Paul.web.app.service.AnswerOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerOptionServiceImpl implements AnswerOptionService {

    @Autowired
    AnswerOptionRepository ansOptRepository;


    @Override
    public AnswerOption saveAnswerOption(AnswerOption ansOpt) {
        return ansOptRepository.save(ansOpt);
    }

    @Override
    public AnswerOption findById(int ansOptId) {
        return ansOptRepository.findById(ansOptId);
    }
}
