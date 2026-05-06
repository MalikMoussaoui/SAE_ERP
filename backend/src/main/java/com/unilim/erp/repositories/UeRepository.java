package com.unilim.erp.repositories;

import com.unilim.erp.entities.Ue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UeRepository extends JpaRepository<Ue, UUID> {
    List<Ue> findByDepartmentId(UUID departmentId);
}
