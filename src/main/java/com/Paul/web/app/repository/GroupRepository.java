package com.Paul.web.app.repository;

import com.Paul.web.app.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    Group findByNumber(String number);
//    Set<Group> findByGroupAdminId();

    @Query(value = "select * from teach_group where group_admin_Id = :admin_id",
            nativeQuery = true)
    Set<Group> findGroupsByAdminId(@Param("admin_id") int adminId);

    Group findById(int groupId);
}
