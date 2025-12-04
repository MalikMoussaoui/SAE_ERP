package com.unilim.erp.service;

import com.unilim.erp.dto.AuthRequest;
import com.unilim.erp.dto.RegistrationRequest;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.domain.UserRole;
import com.unilim.erp.domain.UserStatus;
import com.unilim.erp.repositories.AppUserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AuthService {

    private final AppUserRepository appUserRepository;


    public AuthService(AppUserRepository appUserRepository)
                        {
        this.appUserRepository = appUserRepository;
    }

    public String login(AuthRequest request) {
        var user = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        if (!user.getPasswordHash().equals(request.password())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return "TOKEN_FACTICE_POUR_TEST_FRONTEND";
    }

    public AppUser register(RegistrationRequest request) {
        if (appUserRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        AppUser user = new AppUser();
        user.setEmail(request.email());
        user.setDisplayName(request.displayName());
        user.setPhone(request.phone());
        user.setPasswordHash(request.password()); // Mot de passe en clair (Test)

        user.setRole(request.role() != null ? request.role() : UserRole.ENSEIGNANT);
        user.setStatus(UserStatus.ACTIVE);
        return appUserRepository.save(user);
    }
}
