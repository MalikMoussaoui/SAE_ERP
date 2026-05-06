package com.unilim.erp.repositories;

import com.unilim.erp.entities.ResourceSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResourceSheetRepository extends JpaRepository<ResourceSheet, UUID> {

    List<ResourceSheet> findByDepartment_LabelIgnoreCase(String departmentLabel);

}