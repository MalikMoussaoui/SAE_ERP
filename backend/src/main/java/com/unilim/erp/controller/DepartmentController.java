package com.unilim.erp.controller;

import com.unilim.erp.entities.AppUser;
import com.unilim.erp.entities.Department;
import com.unilim.erp.repositories.AppUserRepository;
import com.unilim.erp.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final AppUserRepository appUserRepository;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody Department department) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé.");
        if (department.getLabel() == null || department.getLabel().isBlank()) {
            return ResponseEntity.badRequest().body("Le label est obligatoire.");
        }
        department.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentRepository.save(department));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable UUID id) {
        if (!isAdmin()) return ResponseEntity.status(403).body("Accès refusé.");
        if (!departmentRepository.existsById(id)) return ResponseEntity.notFound().build();
        departmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = appUserRepository.findByEmail(email).orElse(null);
        return user != null && user.getRole().name().equals("ADMINISTRATEUR");
    }
}
