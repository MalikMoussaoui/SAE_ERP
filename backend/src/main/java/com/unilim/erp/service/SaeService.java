package com.unilim.erp.service;

import com.unilim.erp.entities.Sae;
import com.unilim.erp.repositories.SaeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SaeService {
    private final SaeRepository saeRepository;

    public SaeService(SaeRepository saeRepository) {
        this.saeRepository = saeRepository;
    }

    public List<Sae> findAll() {
        return saeRepository.findAll();
    }

    public List<Sae> findByUeId(UUID ueId) {
        return saeRepository.findByUeId(ueId);
    }
}
