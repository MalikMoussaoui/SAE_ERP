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

    private final AppUserRepository repository;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }

    // --- Méthodes requises par le CRUD du Contrôleur ---

    public List<AppUser> getAllAppUsers() {
        return repository.findAll();
    }

    public Optional<AppUser> getAppUserById(UUID id) {
        return repository.findById(id);
    }

    public AppUser createAppUser(AppUser appUser) {
        // Vous pouvez ajouter ici le hachage du mot de passe avant la sauvegarde
        return repository.save(appUser);
    }

    public AppUser updateAppUser(UUID id, AppUser appUser) {
        if (repository.existsById(id)) {
            appUser.setId(id); // Assure que l'ID reste celui de l'URL
            return repository.save(appUser);
        } else {
            throw new RuntimeException("Utilisateur non trouvé");
        }
    }

    public void deleteAppUser(UUID id) {
        repository.deleteById(id);
    }

    // --- Vos méthodes métiers spécifiques (conservées) ---

    public Optional<AppUser> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional
    public AppUser updateUserStatus(UUID id, UserStatus status) {
        return repository.findById(id)
                .map(user -> {
                    user.setStatus(status);
                    return repository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + id));
    }

    @Transactional
    public AppUser updateProfile(UUID id, String displayName, String phone) {
        return repository.findById(id)
                .map(user -> {
                    if (displayName != null) {
                        user.setDisplayName(displayName);
                    }
                    if (phone != null) {
                        user.setPhone(phone);
                    }
                    return repository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + id));
    }
}
