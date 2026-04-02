package com.unilim.erp.controller;

import com.unilim.erp.entities.AppUser;
import com.unilim.erp.entities.Department;
import com.unilim.erp.entities.Ue;
import com.unilim.erp.repositories.AppUserRepository;
import com.unilim.erp.repositories.DepartmentRepository;
import com.unilim.erp.repositories.UeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ues")
@RequiredArgsConstructor
public class UeController {

    private final UeRepository ueRepository;
    private final DepartmentRepository departmentRepository;
    private final AppUserRepository appUserRepository;

    @GetMapping
    public ResponseEntity<List<Ue>> getAllUes(@RequestParam(required = false) UUID departmentId) {
        if (departmentId != null) {
            return ResponseEntity.ok(ueRepository.findByDepartmentId(departmentId));
        }
        return ResponseEntity.ok(ueRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createUe(@RequestBody UeRequest request) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé.");
        if (request.title == null || request.title.isBlank()) return ResponseEntity.badRequest().body("Le titre est obligatoire.");
        if (request.departmentId == null) return ResponseEntity.badRequest().body("Le département est obligatoire.");

        Department dept = departmentRepository.findById(request.departmentId).orElse(null);
        if (dept == null) return ResponseEntity.badRequest().body("Département introuvable.");

        Ue ue = new Ue(dept, request.title, request.semester, request.objectives);
        return ResponseEntity.status(HttpStatus.CREATED).body(ueRepository.save(ue));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUe(@PathVariable UUID id, @RequestBody UeRequest request) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé.");
        Ue ue = ueRepository.findById(id).orElse(null);
        if (ue == null) return ResponseEntity.notFound().build();

        if (request.title != null && !request.title.isBlank()) ue.setTitle(request.title);
        if (request.semester > 0) ue.setSemester(request.semester);
        if (request.objectives != null) ue.setObjectives(request.objectives);
        if (request.departmentId != null) {
            Department dept = departmentRepository.findById(request.departmentId).orElse(null);
            if (dept != null) ue.setDepartment(dept);
        }
        return ResponseEntity.ok(ueRepository.save(ue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUe(@PathVariable UUID id) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé.");
        if (!ueRepository.existsById(id)) return ResponseEntity.notFound().build();
        ueRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = appUserRepository.findByEmail(email).orElse(null);
        return user != null && user.getRole().name().equals("ADMINISTRATEUR");
    }

    static class UeRequest {
        public String title;
        public int semester;
        public String objectives;
        public UUID departmentId;
    }
}
