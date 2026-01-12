package com.unilim.erp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unilim.erp.dto.McccDto;
import com.unilim.erp.entities.Mccc;
import com.unilim.erp.repositories.McccRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class McccService {

    private final McccRepository mcccRepository;
    private final ObjectMapper objectMapper;

    public McccService(McccRepository mcccRepository, ObjectMapper objectMapper) {
        this.mcccRepository = mcccRepository;
        this.objectMapper = objectMapper;
    }

    public List<McccDto> getAll() {
        return mcccRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public McccDto getById(UUID id) {
        return mcccRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("MCCC not found"));
    }

    public McccDto create(McccDto dto) {
        Mccc entity = convertToEntity(dto);
        return convertToDto(mcccRepository.save(entity));
    }

    public McccDto update(UUID id, McccDto dto) {
        return mcccRepository.findById(id)
                .map(existing -> {
                    updateEntityFromDto(existing, dto);
                    return convertToDto(mcccRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("MCCC not found"));
    }

    public void delete(UUID id) {
        mcccRepository.deleteById(id);
    }

    private McccDto convertToDto(Mccc entity) {
        McccDto dto = new McccDto();
        dto.setId(entity.getId());
        dto.setDepartment(entity.getDepartment());
        dto.setUe(entity.getUe());
        dto.setYears(entity.getYears());
        dto.setSemester(entity.getSemester());
        dto.setSavedAt(entity.getSavedAt());

        try {
            if (entity.getForm() != null) {
                dto.setForm(objectMapper.readValue(entity.getForm(), new TypeReference<Map<String, Object>>() {
                }));
            }
            if (entity.getRessourcesRows() != null) {
                dto.setRessourcesRows(objectMapper.readValue(entity.getRessourcesRows(),
                        new TypeReference<List<Map<String, Object>>>() {
                        }));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return dto;
    }

    private Mccc convertToEntity(McccDto dto) {
        Mccc entity = new Mccc();
        updateEntityFromDto(entity, dto);
        return entity;
    }

    private void updateEntityFromDto(Mccc entity, McccDto dto) {
        entity.setDepartment(dto.getDepartment());
        entity.setUe(dto.getUe());
        entity.setYears(dto.getYears());
        entity.setSemester(dto.getSemester());

        try {
            if (dto.getForm() != null) {
                entity.setForm(objectMapper.writeValueAsString(dto.getForm()));
            }
            if (dto.getRessourcesRows() != null) {
                entity.setRessourcesRows(objectMapper.writeValueAsString(dto.getRessourcesRows()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing JSON", e);
        }
    }
}
