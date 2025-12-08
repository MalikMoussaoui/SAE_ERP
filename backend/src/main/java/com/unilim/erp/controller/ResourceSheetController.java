package com.unilim.erp.controller;

import com.unilim.erp.entities.ResourceSheet;
import com.unilim.erp.service.ResourceSheetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/resource-sheets")
@CrossOrigin(origins = "*")
public class ResourceSheetController{
    private final ResourceSheetService service;

    public ResourceSheetController ( ResourceSheetService service){
        this.service=service;
    }

    @GetMapping
    public List<ResourceSheet> getAll(){
        return service.getAllResourceSheets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceSheet> getById(@PathVariable UUID id) {
        return service.getResourceSheetById(id)
                .map(resourceSheet -> new ResponseEntity<>(resourceSheet, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ResourceSheet> create(@RequestBody ResourceSheet resourceSheet) {
        ResourceSheet createdResourceSheet = service.createResourceSheet(resourceSheet);
        return new ResponseEntity<>(createdResourceSheet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceSheet> update(@PathVariable UUID id, @RequestBody ResourceSheet resourceSheet) {
    ResourceSheet updatedResourceSheet = service.updateResourceSheet(id, resourceSheet);
    return new ResponseEntity<>(updatedResourceSheet, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.deleteResourceSheet(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
