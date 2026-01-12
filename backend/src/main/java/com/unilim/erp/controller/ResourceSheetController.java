package com.unilim.erp.controller;

import com.unilim.erp.dto.ResourceSheetDto;
import com.unilim.erp.entities.ResourceSheet;
import com.unilim.erp.repositories.ResourceSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resource-sheets")
@CrossOrigin(origins = "*")
public class ResourceSheetController {

    @Autowired
    private ResourceSheetRepository resourceSheetRepository;

    @GetMapping
    public List<ResourceSheet> getAll() {
        return resourceSheetRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceSheet> getOne(@PathVariable UUID id) {
        return resourceSheetRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ResourceSheet> create(@RequestBody ResourceSheetDto dto) {
        ResourceSheet sheet = new ResourceSheet();
        updateEntityFromDto(sheet, dto); // On utilise une méthode commune pour remplir
        return ResponseEntity.ok(resourceSheetRepository.save(sheet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceSheet> update(@PathVariable UUID id, @RequestBody ResourceSheetDto dto) {
        // 1. On cherche la fiche existante
        return resourceSheetRepository.findById(id)
                .map(existingSheet -> {
                    // 2. On met à jour ses infos
                    updateEntityFromDto(existingSheet, dto);
                    // 3. On sauvegarde (JPA comprend que c'est une mise à jour car l'ID existe déjà)
                    return ResponseEntity.ok(resourceSheetRepository.save(existingSheet));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (resourceSheetRepository.existsById(id)) {
            resourceSheetRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private void updateEntityFromDto(ResourceSheet sheet, ResourceSheetDto dto) {
        sheet.setTitle(dto.getTitre());
        sheet.setDepartmentName(dto.getDepartement());
        sheet.setObjectives(dto.getDescription());
        sheet.setHoursCm(dto.getHCM());
        sheet.setHoursTd(dto.getHTD());
        sheet.setHoursTp(dto.getHTP());

        if (sheet.getStatus() == null) sheet.setStatus("DRAFT");
        if (sheet.getVersion() == 0) sheet.setVersion(1);
    }
}