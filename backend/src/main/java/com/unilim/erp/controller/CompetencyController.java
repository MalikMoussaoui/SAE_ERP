package com.unilim.erp.controller;

import com.unilim.erp.entities.Competency;
import com.unilim.erp.service.CompetencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competencies")
@CrossOrigin(origins = "*")
public class CompetencyController {
    private final CompetencyService service;

    public CompetencyController(CompetencyService service) {
        this.service = service;
    }

    @GetMapping
    public List<Competency> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Competency> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(competency -> new ResponseEntity<>(competency, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Competency> create(@RequestBody Competency competency) {
        Competency createdCompetency = service.create(competency);
        return new ResponseEntity<>(createdCompetency, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Competency> update(@PathVariable UUID id, @RequestBody Competency competency) {
        try {
            Competency updatedCompetency = service.update(id, competency);
            return new ResponseEntity<>(updatedCompetency, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
