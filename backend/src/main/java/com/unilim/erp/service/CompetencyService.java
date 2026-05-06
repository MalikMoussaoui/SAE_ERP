package com.unilim.erp.service;

import com.unilim.erp.entities.Competency;
import com.unilim.erp.exceptions.CompetencyNotFoundException;
import com.unilim.erp.repositories.CompetencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompetencyService {
    private final CompetencyRepository competencyRepository;

    public CompetencyService(CompetencyRepository competencyRepository) {
        this.competencyRepository = competencyRepository;
    }

    public List<Competency> findAll() {
        return competencyRepository.findAll();
    }

    public Optional<Competency> findById(UUID id) {
        return competencyRepository.findById(id);
    }

    public Competency create(Competency competency) {
        return competencyRepository.save(competency);
    }

    public Competency update(UUID id, Competency competency) {
        // on teste dabord l'exeption
        if (!competencyRepository.existsById(id)) {
            throw new CompetencyNotFoundException(id);
        }
        competency.setId(id);
        return competencyRepository.save(competency);
    }

    public void delete(UUID id) {
        competencyRepository.deleteById(id);
    }
}
