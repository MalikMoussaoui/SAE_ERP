package com.unilim.erp.controller;

import com.unilim.erp.dto.McccDto;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.repositories.AppUserRepository;
import com.unilim.erp.service.McccService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mccc")
@CrossOrigin(origins = "*")
public class McccController {

    private final McccService mcccService;
    private final AppUserRepository appUserRepository;

    public McccController(McccService mcccService, AppUserRepository appUserRepository) {
        this.mcccService = mcccService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping
    public List<McccDto> getAll() {
        return mcccService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<McccDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(mcccService.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody McccDto dto) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé : seul l'administrateur peut créer un MCCC.");
        return ResponseEntity.status(HttpStatus.CREATED).body(mcccService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody McccDto dto) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé : seul l'administrateur peut modifier un MCCC.");
        return ResponseEntity.ok(mcccService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé : seul l'administrateur peut supprimer un MCCC.");
        mcccService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = appUserRepository.findByEmail(email).orElse(null);
        return user != null && user.getRole().name().equals("ADMINISTRATEUR");
    }
}
