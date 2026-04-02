package com.unilim.erp.controller;

import com.unilim.erp.dto.ResourceSheetDto;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.entities.ResourceSheet;
import com.unilim.erp.entities.Department;
import com.unilim.erp.entities.Ue;
import com.unilim.erp.repositories.AppUserRepository;
import com.unilim.erp.repositories.DepartmentRepository;
import com.unilim.erp.repositories.ResourceSheetRepository;
import com.unilim.erp.repositories.UeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resource-sheets")
@RequiredArgsConstructor
public class ResourceSheetController {

    private final ResourceSheetRepository resourceSheetRepository;
    private final AppUserRepository appUserRepository;
    private final DepartmentRepository departmentRepository;
    private final UeRepository ueRepository;

    @GetMapping
    public List<ResourceSheetDto> getAllResourceSheets() {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser currentUser = appUserRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String role = currentUser.getRole().name();

        if (role.equals("ADMINISTRATEUR") || role.equals("RH") || role.equals("DIRECTION")) {
            return resourceSheetRepository.findAll().stream()
                    .map(this::mapToDto)
                    .toList();
        }

        if (role.equals("RESPONSABLE_PEDAGOGIQUE")) {
            return resourceSheetRepository.findAll().stream()
                    .filter(sheet -> {
                        boolean isManager = isUserMatchedInField(currentUser, sheet.getResponsablePedagogique());
                        boolean isIntervenant = isUserMatchedInField(currentUser, sheet.getIntervenants());
                        boolean isCreator = sheet.getCreatedBy() != null
                                && sheet.getCreatedBy().equalsIgnoreCase(currentEmail);

                        boolean isInSameDept = currentUser.getDepartment() != null &&
                                sheet.getDepartment() != null &&
                                currentUser.getDepartment().getLabel()
                                        .equalsIgnoreCase(sheet.getDepartment().getLabel());

                        return isManager || isIntervenant || isCreator || isInSameDept;
                    })
                    .map(this::mapToDto)
                    .toList();
        }

        return resourceSheetRepository.findAll().stream()
                .filter(sheet -> {
                    boolean isManager = isUserMatchedInField(currentUser, sheet.getResponsablePedagogique());
                    boolean isIntervenant = isUserMatchedInField(currentUser, sheet.getIntervenants());
                    boolean isCreator = sheet.getCreatedBy() != null
                            && sheet.getCreatedBy().equalsIgnoreCase(currentEmail);
                    return isManager || isIntervenant || isCreator;
                })
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
    public ResponseEntity<?> createResourceSheet(@RequestBody ResourceSheetDto resourceSheetDto) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByEmail(currentEmail).orElse(null);

        if (currentUser != null && currentUser.getRole().name().equals("VACATAIRE")) {
            return ResponseEntity.status(403).body("Accès refusé : les vacataires ne peuvent pas créer de fiche.");
        }

        ResourceSheet resourceSheet = new ResourceSheet();
        if (resourceSheetDto.getId() != null) {
            resourceSheet.setId(resourceSheetDto.getId());
        } else {
            resourceSheet.setId(UUID.randomUUID());
        }
        resourceSheet.setCreatedBy(currentEmail);
        mapToEntity(resourceSheetDto, resourceSheet);
        ResourceSheet savedSheet = resourceSheetRepository.save(resourceSheet);
        return ResponseEntity.ok(mapToDto(savedSheet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResourceSheet(@PathVariable UUID id,
            @RequestBody ResourceSheetDto resourceSheetDto) {

        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByEmail(currentEmail).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        String roleName = currentUser.getRole().name();
        boolean isGlobalAdmin = roleName.equals("ADMINISTRATEUR") || roleName.equals("RH");

        return resourceSheetRepository.findById(id)
                .map(existingSheet -> {
                    boolean isResponsable = isUserMatchedInField(currentUser,
                            existingSheet.getResponsablePedagogique());
                    boolean isIntervenant = isUserMatchedInField(currentUser, existingSheet.getIntervenants());
                    boolean isCreator = existingSheet.getCreatedBy() != null
                            && existingSheet.getCreatedBy().equalsIgnoreCase(currentEmail);

                    if (existingSheet.isValidated() && !roleName.equals("ADMINISTRATEUR")) {
                        return ResponseEntity.status(403).body(
                                (Object) "Accès refusé : la fiche est validée et ne peut être modifiée que par un administrateur.");
                    }

                    if (!isGlobalAdmin && !isResponsable && !isIntervenant && !isCreator) {
                        return ResponseEntity.status(403).body(
                                (Object) "Accès refusé : vous devez être responsable, intervenant ou créateur pour modifier cette fiche.");
                    }

                    mapToEntity(resourceSheetDto, existingSheet);
                    ResourceSheet updatedSheet = resourceSheetRepository.save(existingSheet);
                    return ResponseEntity.ok((Object) mapToDto(updatedSheet));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceSheet(@PathVariable UUID id) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByEmail(currentEmail).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        ResourceSheet existingSheet = resourceSheetRepository.findById(id).orElse(null);
        if (existingSheet == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isGlobalAdmin = currentUser.getRole().name().equals("ADMINISTRATEUR");
        boolean isCreator = existingSheet.getCreatedBy() != null
                && existingSheet.getCreatedBy().equalsIgnoreCase(currentEmail);

        if (existingSheet.isValidated() && !isGlobalAdmin) {
            return ResponseEntity.status(403).build();
        }

        if (!isGlobalAdmin && !isCreator) {
            return ResponseEntity.status(403).build();
        }

        resourceSheetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void mapToEntity(ResourceSheetDto dto, ResourceSheet entity) {
        entity.setTitre(dto.getTitre());

        if (dto.getDepartement() != null) {
            Department dept = departmentRepository.findAll().stream()
                    .filter(d -> d.getLabel().equals(dto.getDepartement()))
                    .findFirst().orElse(null);
            entity.setDepartment(dept);
        }

        if (dto.getUe() != null) {
            Ue ue = ueRepository.findAll().stream()
                    .filter(u -> u.getTitle().equals(dto.getUe()))
                    .findFirst().orElse(null);
            entity.setUe(ue);
        }

        entity.setCode(dto.getCode());
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
            String roleName = currentUser.getRole().name();
            boolean isGlobalAdmin = roleName.equals("ADMINISTRATEUR") || roleName.equals("RH");
            boolean isResponsable = false;

            if (roleName.equals("RESPONSABLE_PEDAGOGIQUE")) {
                isResponsable = isUserMatchedInField(currentUser, dto.getResponsablePedagogique()) ||
                        isUserMatchedInField(currentUser, entity.getResponsablePedagogique());
            }


            if (isGlobalAdmin || isResponsable) {
                entity.setValidated(dto.isValidated());
            } else if (entity.getId() == null) {

                entity.setValidated(false);
            }
        }
    }

    private ResourceSheetDto mapToDto(ResourceSheet entity) {
        ResourceSheetDto dto = new ResourceSheetDto();
        dto.setId(entity.getId());
        dto.setTitre(entity.getTitre());
        dto.setDepartement(entity.getDepartment() != null ? entity.getDepartment().getLabel() : null);
        dto.setCode(entity.getCode());
        dto.setUe(entity.getUe() != null ? entity.getUe().getTitle() : null);
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
        dto.setCreatedBy(entity.getCreatedBy());
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

            String roleName = currentUser.getRole().name();
            boolean isGlobalAdmin = roleName.equals("ADMINISTRATEUR") || roleName.equals("RH");

            return resourceSheetRepository.findById(id).map(existingSheet -> {
                boolean isResponsable = false;
                if (roleName.equals("RESPONSABLE_PEDAGOGIQUE")) {
                    isResponsable = isUserMatchedInField(currentUser, existingSheet.getResponsablePedagogique());
                }

                if (!isGlobalAdmin && !isResponsable) {
                    return ResponseEntity.status(403).body(
                            (Object) "Accès refusé : vous n'êtes pas administrateur, RH, ou responsable de cette ressource.");
                }

                try {
                    ResourceSheet newSheet = new ResourceSheet();
                    newSheet.setId(UUID.randomUUID());
                    newSheet.setTitre(existingSheet.getTitre() + " (Copie)");
                    newSheet.setDepartment(existingSheet.getDepartment());
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
                    newSheet.setCreatedBy(currentEmail);
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

    @PostMapping("/{id}/validate")
    public ResponseEntity<?> validateResourceSheet(@PathVariable UUID id) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByEmail(currentEmail).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        String roleName = currentUser.getRole().name();
        boolean isAdminOrRH = roleName.equals("ADMINISTRATEUR") || roleName.equals("RH");
        boolean isResponsable = roleName.equals("RESPONSABLE_PEDAGOGIQUE");

        if (!isAdminOrRH && !isResponsable) {
            return ResponseEntity.status(403)
                    .body((Object) "Accès refusé : seuls les administrateurs, RH et responsables pédagogiques peuvent valider une fiche.");
        }

        return resourceSheetRepository.findById(id)
                .map(existingSheet -> {
                    if (isResponsable && !isUserMatchedInField(currentUser, existingSheet.getResponsablePedagogique())) {
                        return ResponseEntity.status(403)
                                .body((Object) "Accès refusé : vous n'êtes pas le responsable pédagogique de cette fiche.");
                    }
                    existingSheet.setValidated(true);
                    ResourceSheet updatedSheet = resourceSheetRepository.save(existingSheet);
                    return ResponseEntity.ok((Object) mapToDto(updatedSheet));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private boolean isUserMatchedInField(AppUser currentUser, String fieldText) {
        if (fieldText == null || currentUser == null) {
            return false;
        }
        String fieldLower = fieldText.toLowerCase();
        String email = currentUser.getEmail() != null ? currentUser.getEmail().toLowerCase() : "";
        String name = currentUser.getDisplayName() != null ? currentUser.getDisplayName().toLowerCase() : "";


        boolean isMatched = (!email.isEmpty() && fieldLower.contains(email))
                || (!name.isEmpty() && fieldLower.contains(name));


        if (!isMatched && !name.isEmpty()) {
            String[] parts = name.split("\\s+");
            for (String part : parts) {
                if (part.length() > 2 && fieldLower.contains(part)) {
                    return true;
                }
            }
        }
        return isMatched;
    }
}