package com.unilim.erp.service;

import com.unilim.erp.domain.UserRole;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.repositories.AppUserRepository;
import org.springframework.stereotype.Service;
import com.unilim.erp.dto.AppUserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    public List<AppUserDto> getTeachersAndVacataires() {

        List<AppUser> users = repository.findByRoleIn(Arrays.asList(UserRole.TEACHER, UserRole.VACATAIRE));

        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AppUserDto convertToDto(AppUser user) {
        AppUserDto dto = new AppUserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setDisplayName(user.getDisplayName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());

        if (user.getRole() == UserRole.TEACHER) {
            dto.setPoste("Professeur");
        } else if (user.getRole() == UserRole.VACATAIRE) {
            dto.setPoste("Vacataire");
        } else {
            dto.setPoste(user.getRole().name());
        }

        dto.setDepartment("N/A");

        return dto;
    }

}