package com.unilim.erp.controller;

import com.unilim.erp.dto.AuthenticationRequest;
import com.unilim.erp.dto.AuthenticationResponse;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import com.unilim.erp.repositories.AppUserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final AppUserRepository appUserRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        final String jwt = jwtUtils.generateToken(userDetails);

        AppUser user = appUserRepository.findByEmail(request.getEmail()) // Assurez-vous d'avoir findByEmail dans votre repo
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return ResponseEntity.ok(AuthenticationResponse.builder()
                .token(jwt)
                .role(user.getRole().name())
                .name(user.getDisplayName())
                .departement(user.getDepartment() != null ? user.getDepartment().getLabel() : null)
                .build());
    }
}