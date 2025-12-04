package com.unilim.erp.controller;

import com.unilim.erp.entities.Sae;
import com.unilim.erp.service.SaeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sae")
@CrossOrigin(origins = "*")
public class SaeController {
    private final SaeService service;

    public SaeController(SaeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Sae> getAll() {
        return service.findAll();
    }

    @GetMapping("/ue/{ueId}")
    public ResponseEntity<List<Sae>> getByUeId(@PathVariable UUID ueId) {
        List<Sae> saes = service.findByUeId(ueId);
        if (saes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(saes);
    }
}
