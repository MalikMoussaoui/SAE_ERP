package com.unilim.erp.service;

import com.unilim.erp.entities.Resource;
// CORRECTION : 'repositories' (pluriel) pour correspondre à votre dossier
import com.unilim.erp.repositories.ResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    public List<Resource> findByUeId(UUID ueId) {
        return resourceRepository.findByUeId(ueId);
    }

    public Optional<Resource> findById(UUID id) {
        return resourceRepository.findById(id);
    }
}