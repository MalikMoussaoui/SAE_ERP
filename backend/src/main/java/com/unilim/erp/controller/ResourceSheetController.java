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
                    .toList();
        }

        return resourceSheetRepository.findByDepartementIgnoreCase(currentUser.getDepartement()).stream()
                .map(this::mapToDto)
                .toList();
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
        if (resourceSheetDto.getId() != null) {
            resourceSheet.setId(resourceSheetDto.getId());
        } else {
            resourceSheet.setId(UUID.randomUUID());
        }
        mapToEntity(resourceSheetDto, resourceSheet);
        ResourceSheet savedSheet = resourceSheetRepository.save(resourceSheet);
        return mapToDto(savedSheet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceSheetDto> updateResourceSheet(@PathVariable UUID id,
            @RequestBody ResourceSheetDto resourceSheetDto) {
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
        entity.setRetourEquipePedagogique(dto.getRetourEquipePedagogique());
        entity.setSequencesRowsJson(dto.getSequencesRowsJson());

        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByEmail(currentEmail).orElse(null);
        if (currentUser != null) {
            boolean isAdminOrResponsable = currentUser.getRole().name().equals("ADMINISTRATEUR") ||
                    currentUser.getRole().name().equals("RESPONSABLE_PEDAGOGIQUE");

            // Only admin or responsable can change validation status
            if (isAdminOrResponsable) {
                entity.setValidated(dto.isValidated());
            } else if (entity.getId() == null) {
                // on creation, default to false if not admin/responsable
                entity.setValidated(false);
            }
        }
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
        dto.setRetourEquipePedagogique(entity.getRetourEquipePedagogique());
        dto.setSequencesRowsJson(entity.getSequencesRowsJson());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setValidated(entity.isValidated());

        return dto;
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<?> duplicateResourceSheet(@PathVariable UUID id) {
        try {
            String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            AppUser currentUser = appUserRepository.findByEmail(currentEmail)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            if (!currentUser.getRole().name().equals("ADMINISTRATEUR")
                    && !currentUser.getRole().name().equals("RESPONSABLE_PEDAGOGIQUE")) {
                return ResponseEntity.status(403).body("Accès refusé : rôle insuffisant");
            }

            return resourceSheetRepository.findById(id).map(existingSheet -> {
                try {
                    ResourceSheet newSheet = new ResourceSheet();
                    newSheet.setId(UUID.randomUUID());
                    newSheet.setTitre(existingSheet.getTitre() + " (Copie)");
                    newSheet.setDepartement(existingSheet.getDepartement());
                    newSheet.setCode(existingSheet.getCode());
                    newSheet.setUe(existingSheet.getUe());
                    newSheet.setSemestre(existingSheet.getSemestre());
                    newSheet.setDescription(existingSheet.getDescription());
                    newSheet.setHCM(existingSheet.getHCM());
                    newSheet.setHTD(existingSheet.getHTD());
                    newSheet.setHTP(existingSheet.getHTP());
                    newSheet.setTypeEvaluation(existingSheet.getTypeEvaluation());
                    newSheet.setCoefficientRessource(existingSheet.getCoefficientRessource());
                    newSheet.setEvaluationsPrevues(existingSheet.getEvaluationsPrevues());
                    newSheet.setNoteMinimale(existingSheet.getNoteMinimale());
                    newSheet.setCompensation(existingSheet.getCompensation());
                    newSheet.setRattrapage(existingSheet.getRattrapage());
                    newSheet.setModaliteRattrapage(existingSheet.getModaliteRattrapage());
                    newSheet.setTypeEnseignement(existingSheet.getTypeEnseignement());
                    newSheet.setResponsablePedagogique(existingSheet.getResponsablePedagogique());
                    newSheet.setIntervenants(existingSheet.getIntervenants());
                    newSheet.setRetourEquipePedagogique(existingSheet.getRetourEquipePedagogique());
                    newSheet.setSequencesRowsJson(existingSheet.getSequencesRowsJson());
                    newSheet.setValidated(false);

                    ResourceSheet savedSheet = resourceSheetRepository.save(newSheet);
                    return ResponseEntity.ok((Object) mapToDto(savedSheet));
                } catch (Exception e) {
                    System.err.println("[DUPLICATE ERROR] " + e.getMessage());
                    e.printStackTrace();
                    return ResponseEntity.status(500).body((Object) ("Erreur duplication: " + e.getMessage()));
                }
            }).orElse(ResponseEntity.status(404).body("Fiche non trouvée"));
        } catch (Exception e) {
            System.err.println("[DUPLICATE OUTER ERROR] " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur interne: " + e.getMessage());
        }
    }
}