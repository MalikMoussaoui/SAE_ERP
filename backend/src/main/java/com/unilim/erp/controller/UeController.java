package com.unilim.erp.controller;

import com.unilim.erp.entities.Ue;
import com.unilim.erp.repositories.UeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ues")
@RequiredArgsConstructor
public class UeController {

    private final UeRepository ueRepository;

    @GetMapping
    public ResponseEntity<List<Ue>> getAllUes() {
        return ResponseEntity.ok(ueRepository.findAll());
    }
}
