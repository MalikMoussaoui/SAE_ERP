package com.unilim.erp.controller;

import com.unilim.erp.dto.AuthRequest;
import com.unilim.erp.dto.RegistrationRequest;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody RegistrationRequest request) {
        try {
            AppUser createdUser = service.register(request);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        try {
            String token = service.login(request);

            return new ResponseEntity<>(Collections.singletonMap("token", token), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
