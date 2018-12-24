package com.Paul.web.app.repository;

import com.Paul.web.app.entity.Test;
import com.Paul.web.app.entity.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Integer> {

    @Query(value = "select count(*) from test_attempt where test = :testId and student = :studentId",
            nativeQuery = true)
    int getAttemptNumber(@Param("testId") int testId, @Param("studentId") int studentId);

    @Query(value = "select * from test_attempt where test = :testId and student = :studentId",
    nativeQuery = true)
    Set<TestAttempt> getStudentTestAttempts(@Param("studentId") int studentId, @Param("testId") int testId);

    TestAttempt findById(int attemptId);

    @Query(value = "select * from test_attempt left join test on test.id = test_attempt.id",
            nativeQuery = true)
    Set<TestAttempt> findByTestManagerId(int testManagerId);
}
