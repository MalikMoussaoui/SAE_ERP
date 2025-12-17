package com.unilim.erp.service;

import com.unilim.erp.entities.AppUser;
import com.unilim.erp.repositories.AppUserRepository; // Vérifie si c'est 'repository' ou 'repositories' dans ton projet
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserRepository repository;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }

    public List<AppUser> getAllAppUsers() {
        return repository.findAll();
    }

    public Optional<AppUser> getAppUserById(UUID id) {
        return repository.findById(id);
    }

    public AppUser createAppUser(AppUser appUser) {
        return repository.save(appUser);
    }

    public AppUser updateAppUser(UUID id, AppUser userDetails) {
        return repository.findById(id).map(existingUser -> {
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setDisplayName(userDetails.getDisplayName());
            existingUser.setPhone(userDetails.getPhone());
            existingUser.setRole(userDetails.getRole());
            existingUser.setStatus(userDetails.getStatus());

            return repository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + id));
    }
    public void deleteAppUser(UUID id) {
        repository.deleteById(id);
    }
}