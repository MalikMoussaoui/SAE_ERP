package com.unilim.erp.repositories;

import com.unilim.erp.entities.Sae;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SaeRepository extends JpaRepository<Sae, UUID> {
    List<Sae> findByUeId(UUID ueId);
}
