package com.Paul.web.app.repository;

import com.Paul.web.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String roleName);

}
