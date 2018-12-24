package com.Paul.web.app.repository;

import com.Paul.web.app.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {

    @Query(value = "select count(*) from student_answer " +
            "left join answer_option " +
            "on answer_option.id = student_answer.answer_option " +
            "where student_answer.attempt = :attempt and answer_option.task = :task",
    nativeQuery = true)
    int findStudentAnswerForTask(@Param("attempt") int attemptId, @Param("task") int taskId);
}
