package com.unilim.erp.service;

import com.unilim.erp.dto.ResourceSheetDto;
import com.unilim.erp.entities.ResourceSheet;
import com.unilim.erp.entities.Department;
import com.unilim.erp.entities.Ue;
import com.unilim.erp.repositories.DepartmentRepository;
import com.unilim.erp.repositories.ResourceSheetRepository;
import com.unilim.erp.repositories.UeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceSheetService {

    private final ResourceSheetRepository repository;
    private final DepartmentRepository departmentRepository;
    private final UeRepository ueRepository;

    public List<ResourceSheetDto> getAll() {
        return repository.findAll().stream().map(this::mapToDto).toList();
    }

    public ResourceSheetDto getById(UUID id) {
        return repository.findById(id).map(this::mapToDto).orElseThrow();
    }

    public ResourceSheetDto create(ResourceSheetDto dto) {
        ResourceSheet entity = new ResourceSheet();
        mapToEntity(dto, entity);
        return mapToDto(repository.save(entity));
    }

    public ResourceSheetDto update(UUID id, ResourceSheetDto dto) {
        ResourceSheet entity = repository.findById(id).orElseThrow();
        mapToEntity(dto, entity);
        return mapToDto(repository.save(entity));
    }

    public void delete(UUID id) {
        repository.deleteById(id);
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

        entity.setDescription(dto.getDescription());
        entity.setHCM(dto.getHCM());
        entity.setHTD(dto.getHTD());
        entity.setHTP(dto.getHTP());
        entity.setCode(dto.getCode());
        entity.setSemestre(dto.getSemestre());
        entity.setTypeEvaluation(dto.getTypeEvaluation());
        entity.setCoefficientRessource(dto.getCoefficientRessource());
        entity.setEvaluationsPrevues(dto.getEvaluationsPrevues());
        entity.setNoteMinimale(dto.getNoteMinimale());
        entity.setCompensation(dto.getCompensation());
        entity.setRattrapage(dto.getRattrapage());
        entity.setModaliteRattrapage(dto.getModaliteRattrapage());
        entity.setResponsablePedagogique(dto.getResponsablePedagogique());
        entity.setIntervenants(dto.getIntervenants());
        entity.setRetourEquipePedagogique(dto.getRetourEquipePedagogique());
        entity.setTypeEnseignement(dto.getTypeEnseignement()); // Remplace setModalities
        entity.setSequencesRowsJson(dto.getSequencesRowsJson());
    }

    private ResourceSheetDto mapToDto(ResourceSheet entity) {
        ResourceSheetDto dto = new ResourceSheetDto();
        dto.setId(entity.getId());
        dto.setTitre(entity.getTitre());
        dto.setDepartement(entity.getDepartment() != null ? entity.getDepartment().getLabel() : null);
        dto.setDescription(entity.getDescription());
        dto.setHCM(entity.getHCM());
        dto.setHTD(entity.getHTD());
        dto.setHTP(entity.getHTP());
        dto.setCode(entity.getCode());
        dto.setUe(entity.getUe() != null ? entity.getUe().getTitle() : null);
        dto.setSemestre(entity.getSemestre());
        dto.setTypeEvaluation(entity.getTypeEvaluation());
        dto.setCoefficientRessource(entity.getCoefficientRessource());
        dto.setEvaluationsPrevues(entity.getEvaluationsPrevues());
        dto.setNoteMinimale(entity.getNoteMinimale());
        dto.setCompensation(entity.getCompensation());
        dto.setRattrapage(entity.getRattrapage());
        dto.setModaliteRattrapage(entity.getModaliteRattrapage());
        dto.setResponsablePedagogique(entity.getResponsablePedagogique());
        dto.setIntervenants(entity.getIntervenants());
        dto.setRetourEquipePedagogique(entity.getRetourEquipePedagogique());
        dto.setTypeEnseignement(entity.getTypeEnseignement());
        dto.setSequencesRowsJson(entity.getSequencesRowsJson());
        return dto;
    }
}
