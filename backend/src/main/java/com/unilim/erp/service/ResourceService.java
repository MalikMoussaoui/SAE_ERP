package com.unilim.erp.service;

import com.unilim.erp.entities.Resource;
import com.unilim.erp.repositories.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResourceService {
    private final ResourceRepository ResourceRepository;

    public ResourceService(ResourceRepository ecRepository) {
        this.ResourceRepository = ecRepository;
    }

    public List<Resource> findAll() {
        return ResourceRepository.findAll();
    }

    public List<Resource> findByUeId(UUID ueId) {
        return ResourceRepository.findByUeId(ueId);
    }
}
