package com.unilim.erp.controller;

import com.unilim.erp.entities.Resource;
import com.unilim.erp.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<Resource> getAll() {
        return resourceService.findAll();
    }

    @GetMapping("/ue/{ueId}")
    public List<Resource> getByUe(@PathVariable UUID ueId) {
        return resourceService.findByUeId(ueId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Resource>> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(resourceService.findById(id));
    }
}
