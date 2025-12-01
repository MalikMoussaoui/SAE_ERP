package com.unilim.erp.service;

import com.unilim.erp.domain.UserStatus;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.repositories.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @Transactional
    public AppUser updateUserStatus(UUID id, UserStatus status) {
        return appUserRepository.findById(id)
                .map(user -> {
                    user.setStatus(status);
                    return appUserRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + id));
    }

    @Transactional
    public AppUser updateProfile(UUID id, String displayName, String phone) {
        return appUserRepository.findById(id)
                .map(user -> {
                    if (displayName != null) {
                        user.setDisplayName(displayName);
                    }
                    if (phone != null) {
                        user.setPhone(phone);
                    }
                    return appUserRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + id));
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }
}
