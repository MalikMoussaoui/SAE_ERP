package com.unilim.erp.controller;

import com.unilim.erp.entities.Tac;
import com.unilim.erp.service.TacService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tac")
@CrossOrigin(origins = "*")
public class TacController {

    private final TacService tacService;

    public TacController(TacService tacService) {
        this.tacService = tacService;
    }

    @GetMapping
    public List<Tac> getAll() {
        return tacService.getAllEntries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tac> getOne(@PathVariable UUID id) {
        try {
            Tac entry = tacService.getEntryById(id);
            return ResponseEntity.ok(entry);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Tac> create(@RequestBody Tac tacEntry) {
        Tac createdEntry = tacService.createEntry(tacEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tac> update(@PathVariable UUID id, @RequestBody Tac tacEntry) {
        try {
            Tac updatedEntry = tacService.updateEntry(id, tacEntry);
            return ResponseEntity.ok(updatedEntry);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tacService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}