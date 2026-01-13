package com.unilim.erp.controller;

import com.unilim.erp.entities.AppUser;
import com.unilim.erp.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unilim.erp.dto.AppUserDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/app-users")
@CrossOrigin(origins = "*")
public class AppUserController {
    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppUser> getAll() {
        return service.getAllAppUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getById(@PathVariable UUID id) {
        return service.getAppUserById(id)
                .map(appUser -> new ResponseEntity<>(appUser, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AppUser> create(@RequestBody AppUser appUser) {
        AppUser createdAppUser = service.createAppUser(appUser);
        return new ResponseEntity<>(createdAppUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> update(@PathVariable UUID id, @RequestBody AppUser appUser) {
        try {
            AppUser updatedAppUser = service.updateAppUser(id, appUser);
            return new ResponseEntity<>(updatedAppUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<AppUserDto>> getTeachers() {
        return ResponseEntity.ok(service.getTeachersAndVacataires());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteAppUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}