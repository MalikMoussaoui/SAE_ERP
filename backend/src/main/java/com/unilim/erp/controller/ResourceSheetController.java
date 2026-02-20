package com.unilim.erp.controller;

import com.unilim.erp.dto.ResourceSheetDto;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.entities.ResourceSheet;
import com.unilim.erp.repositories.AppUserRepository;
import com.unilim.erp.repositories.ResourceSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resource-sheets")
@RequiredArgsConstructor
public class ResourceSheetController {

    private final ResourceSheetRepository resourceSheetRepository;
    private final AppUserRepository appUserRepository;

    @GetMapping
    public List<ResourceSheetDto> getAllResourceSheets() {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser currentUser = appUserRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (currentUser.getRole().name().contains("ADMIN")) {
            return resourceSheetRepository.findAll().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        //temporaire le temps de mettre en place les mêmes départements dans app_user et resource_sheet

        return resourceSheetRepository.findByDepartementIgnoreCase(currentUser.getDepartement()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceSheetDto> getResourceSheetById(@PathVariable UUID id) {
        return resourceSheetRepository.findById(id)
                .map(this::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResourceSheetDto createResourceSheet(@RequestBody ResourceSheetDto resourceSheetDto) {
        ResourceSheet resourceSheet = new ResourceSheet();
        mapToEntity(resourceSheetDto, resourceSheet);
        ResourceSheet savedSheet = resourceSheetRepository.save(resourceSheet);
        return mapToDto(savedSheet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceSheetDto> updateResourceSheet(@PathVariable UUID id, @RequestBody ResourceSheetDto resourceSheetDto) {
        return resourceSheetRepository.findById(id)
                .map(existingSheet -> {
                    mapToEntity(resourceSheetDto, existingSheet);
                    ResourceSheet updatedSheet = resourceSheetRepository.save(existingSheet);
                    return ResponseEntity.ok(mapToDto(updatedSheet));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceSheet(@PathVariable UUID id) {
        if (resourceSheetRepository.existsById(id)) {
            resourceSheetRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private void mapToEntity(ResourceSheetDto dto, ResourceSheet entity) {
        entity.setTitre(dto.getTitre());
        entity.setDepartement(dto.getDepartement());
        entity.setCode(dto.getCode());
        entity.setUe(dto.getUe());
        entity.setSemestre(dto.getSemestre());
        entity.setDescription(dto.getDescription());
        entity.setHCM(dto.getHCM());
        entity.setHTD(dto.getHTD());
        entity.setHTP(dto.getHTP());
        entity.setTypeEvaluation(dto.getTypeEvaluation());
        entity.setCoefficientRessource(dto.getCoefficientRessource());
        entity.setEvaluationsPrevues(dto.getEvaluationsPrevues());
        entity.setNoteMinimale(dto.getNoteMinimale());
        entity.setCompensation(dto.getCompensation());
        entity.setRattrapage(dto.getRattrapage());
        entity.setModaliteRattrapage(dto.getModaliteRattrapage());
        entity.setTypeEnseignement(dto.getTypeEnseignement());
        entity.setResponsablePedagogique(dto.getResponsablePedagogique());
        entity.setIntervenants(dto.getIntervenants());
        entity.setSequencesRowsJson(dto.getSequencesRowsJson());
    }

    private ResourceSheetDto mapToDto(ResourceSheet entity) {
        ResourceSheetDto dto = new ResourceSheetDto();
        dto.setId(entity.getId());
        dto.setTitre(entity.getTitre());
        dto.setDepartement(entity.getDepartement());
        dto.setCode(entity.getCode());
        dto.setUe(entity.getUe());
        dto.setSemestre(entity.getSemestre());
        dto.setDescription(entity.getDescription());
        dto.setHCM(entity.getHCM());
        dto.setHTD(entity.getHTD());
        dto.setHTP(entity.getHTP());
        dto.setTypeEvaluation(entity.getTypeEvaluation());
        dto.setCoefficientRessource(entity.getCoefficientRessource());
        dto.setEvaluationsPrevues(entity.getEvaluationsPrevues());
        dto.setNoteMinimale(entity.getNoteMinimale());
        dto.setCompensation(entity.getCompensation());
        dto.setRattrapage(entity.getRattrapage());
        dto.setModaliteRattrapage(entity.getModaliteRattrapage());
        dto.setTypeEnseignement(entity.getTypeEnseignement());
        dto.setResponsablePedagogique(entity.getResponsablePedagogique());
        dto.setIntervenants(entity.getIntervenants());
        dto.setSequencesRowsJson(entity.getSequencesRowsJson());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}