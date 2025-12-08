package com.unilim.erp.service;

import com.unilim.erp.entities.ResourceSheet;
import com.unilim.erp.repositories.ResourceSheetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResourceSheetService {

    private final ResourceSheetRepository resourceSheetRepository;


    public ResourceSheetService(ResourceSheetRepository resourceSheetRepository) {
        this.resourceSheetRepository = resourceSheetRepository;
    }

    public List<ResourceSheet> getAllResourceSheets() {
        return resourceSheetRepository.findAll();
    }

    public Optional<ResourceSheet> getResourceSheetById(UUID id) {
        return resourceSheetRepository.findById(id);
    }

    public ResourceSheet createResourceSheet(ResourceSheet resourceSheet) {
        return resourceSheetRepository.save(resourceSheet);
    }

    // Modifier une fiche existante
    public ResourceSheet updateResourceSheet(UUID id, ResourceSheet resourceSheetDetails) {
        return resourceSheetRepository.findById(id)
                .map(existingSheet -> {
                    existingSheet.setObjectives(resourceSheetDetails.getObjectives());
                    existingSheet.setPrerequisites(resourceSheetDetails.getPrerequisites());
                    existingSheet.setModalities(resourceSheetDetails.getModalities());
                    existingSheet.setHoursCm(resourceSheetDetails.getHoursCm());
                    existingSheet.setHoursTd(resourceSheetDetails.getHoursTd());
                    existingSheet.setHoursTp(resourceSheetDetails.getHoursTp());
                    return resourceSheetRepository.save(existingSheet);
                })
                .orElseThrow(() -> new RuntimeException("Fiche introuvable avec l'ID : " + id));
    }

    public void deleteResourceSheet(UUID id) {
        resourceSheetRepository.deleteById(id);
    }
}