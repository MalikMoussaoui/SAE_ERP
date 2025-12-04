package com.unilim.erp.controller;
import com.unilim.erp.entities.Ue;
import com.unilim.erp.service.UeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ue")
@CrossOrigin(origins = "*")
public class UeController {
    private final UeService service;

    public UeController(UeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ue> getAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Ue> create(@RequestBody Ue ue) {
        Ue createdUe = service.createUe(ue);
        return new ResponseEntity<>(createdUe, HttpStatus.CREATED);
    }
}
