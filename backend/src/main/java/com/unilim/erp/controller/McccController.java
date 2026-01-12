package com.unilim.erp.controller;

import com.unilim.erp.dto.McccDto;
import com.unilim.erp.service.McccService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mccc")
@CrossOrigin(origins = "*")
public class McccController {

    private final McccService mcccService;

    public McccController(McccService mcccService) {
        this.mcccService = mcccService;
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
    public ResponseEntity<McccDto> create(@RequestBody McccDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mcccService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<McccDto> update(@PathVariable UUID id, @RequestBody McccDto dto) {
        return ResponseEntity.ok(mcccService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        mcccService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
