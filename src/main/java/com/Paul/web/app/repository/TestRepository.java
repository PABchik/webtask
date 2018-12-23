package com.Paul.web.app.repository;

import com.Paul.web.app.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TestRepository extends JpaRepository<Test, Integer> {
    Test findByName(String name);

    Test findById(int id);

    @Query(value = "select * from test where organisation_id = :orgId",
            nativeQuery = true)
    Set<Test> findAllOrgTests(@Param("orgId") int orgId);

    @Query(value = "select * from test where manager_id = :managerId",
            nativeQuery = true)
    Set<Test> findByTestManagerId(int id);
}
