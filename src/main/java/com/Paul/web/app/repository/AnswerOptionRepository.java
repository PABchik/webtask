package com.Paul.web.app.repository;

import com.Paul.web.app.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Integer> {
    AnswerOption findById(int ansOptId);
}
