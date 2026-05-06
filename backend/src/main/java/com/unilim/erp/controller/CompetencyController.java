package com.unilim.erp.controller;

import com.unilim.erp.entities.AppUser;
import com.unilim.erp.entities.Competency;
import com.unilim.erp.repositories.AppUserRepository;
import com.unilim.erp.service.CompetencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competencies")
@CrossOrigin(origins = "*")
public class CompetencyController {
    private final CompetencyService service;
    private final AppUserRepository appUserRepository;

    public CompetencyController(CompetencyService service, AppUserRepository appUserRepository) {
        this.service = service;
        this.appUserRepository = appUserRepository;
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
    public ResponseEntity<?> create(@RequestBody Competency competency) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé : seul l'administrateur peut créer une compétence.");
        return new ResponseEntity<>(service.create(competency), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Competency competency) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé : seul l'administrateur peut modifier une compétence.");
        try {
            return new ResponseEntity<>(service.update(id, competency), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé : seul l'administrateur peut supprimer une compétence.");
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean isAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = appUserRepository.findByEmail(email).orElse(null);
        return user != null && user.getRole().name().equals("ADMINISTRATEUR");
    }
}
