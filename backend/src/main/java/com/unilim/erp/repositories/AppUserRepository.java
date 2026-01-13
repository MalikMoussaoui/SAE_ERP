package com.unilim.erp.repositories;

import com.unilim.erp.domain.UserRole;
import com.unilim.erp.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByEmail(String displayName);
    List<AppUser> findByRoleIn(List<UserRole> roles);

}