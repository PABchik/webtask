package com.Paul.web.app.repository;

import com.Paul.web.app.entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Integer>{

    Organisation findByName(String name);
    Organisation findByOwnerId(int ownerId);
    Organisation findById(int id);
}
