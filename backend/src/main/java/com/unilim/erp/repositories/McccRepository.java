package com.unilim.erp.repositories;

import com.unilim.erp.entities.Mccc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface McccRepository extends JpaRepository<Mccc, UUID> {
}