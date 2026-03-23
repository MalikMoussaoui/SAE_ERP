package com.unilim.erp.service;

import com.unilim.erp.domain.UserRole;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.unilim.erp.dto.AppUserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<AppUser> getAllAppUsers() {
        return repository.findAll();
    }

    public Optional<AppUser> getAppUserById(UUID id) {
        return repository.findById(id);
    }

    public AppUser createAppUser(AppUser appUser) {
        appUser.setId(null);
        if (appUser.getPasswordHash() != null) {
            appUser.setPasswordHash(passwordEncoder.encode(appUser.getPasswordHash()));
        }
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

    public List<AppUserDto> getTeachersAndVacataires(String departement) {
        List<AppUser> users;
        if (departement != null && !departement.isEmpty()) {
            users = repository.findByRoleInAndDepartment_Label(
                    Arrays.asList(UserRole.TEACHER, UserRole.VACATAIRE), departement);
        } else {
            users = repository.findByRoleIn(Arrays.asList(UserRole.TEACHER, UserRole.VACATAIRE));
        }

        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AppUserDto convertToDto(AppUser user) {
        return AppUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .poste(mapRoleToPoste(user.getRole()))
                .department(user.getDepartment() != null ? user.getDepartment().getLabel() : "N/A")
                .build();
    }

    private String mapRoleToPoste(UserRole role) {
        return switch (role) {
            case TEACHER -> "Professeur";
            case VACATAIRE -> "Vacataire";
            default -> role.name();
        };
    }

}