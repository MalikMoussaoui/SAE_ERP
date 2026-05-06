package com.unilim.erp.service;

import com.unilim.erp.dto.NotificationDTO;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.entities.ResourceSheet;
import com.unilim.erp.repositories.AppUserRepository;
import com.unilim.erp.repositories.ResourceSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ResourceSheetRepository resourceSheetRepository;
    private final AppUserRepository appUserRepository;

    public List<NotificationDTO> getPendingNotificationsForUser() {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByEmail(currentEmail).orElse(null);
        if (currentUser == null) {
            return List.of();
        }

        String role = currentUser.getRole().name();

        return resourceSheetRepository.findAll().stream()
                .filter(sheet -> !sheet.isSubmitted() && !sheet.isValidated())
                .filter(sheet -> canUserAccessSheet(currentUser, role, currentEmail, sheet))
                .sorted(Comparator.comparing(ResourceSheet::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(10)
                .map(sheet -> NotificationDTO.builder()
                        .type("BROUILLON")
                        .message(buildDraftMessage(sheet))
                        .actionUrl("/fiche-ressource/" + sheet.getId())
                        .urgency("HIGH")
                        .entityId(sheet.getId().toString())
                        .build())
                .toList();
    }

    private boolean canUserAccessSheet(AppUser currentUser, String role, String currentEmail, ResourceSheet sheet) {
        if (role.equals("ADMINISTRATEUR") || role.equals("RH") || role.equals("DIRECTION")) {
            return true;
        }

        boolean isCreator = sheet.getCreatedBy() != null && sheet.getCreatedBy().equalsIgnoreCase(currentEmail);
        boolean isManager = isUserMatchedInField(currentUser, sheet.getResponsablePedagogique());
        boolean isIntervenant = isUserMatchedInField(currentUser, sheet.getIntervenants());

        if (role.equals("RESPONSABLE_PEDAGOGIQUE")) {
            boolean sameDept = currentUser.getDepartment() != null
                    && sheet.getDepartment() != null
                    && currentUser.getDepartment().getLabel().equalsIgnoreCase(sheet.getDepartment().getLabel());
            return isCreator || isManager || isIntervenant || sameDept;
        }

        return isCreator || isManager || isIntervenant;
    }

    private boolean isUserMatchedInField(AppUser user, String fieldText) {
        if (user == null || fieldText == null || fieldText.isBlank()) {
            return false;
        }

        String userName = user.getDisplayName() == null ? "" : user.getDisplayName().toLowerCase();
        String haystack = fieldText.toLowerCase();
        if (haystack.contains(userName)) {
            return true;
        }

        String[] parts = userName.split("\\s+");
        for (String part : parts) {
            if (part.length() > 2 && haystack.contains(part)) {
                return true;
            }
        }
        return false;
    }

    private String buildDraftMessage(ResourceSheet sheet) {
        String label = firstNonBlank(sheet.getTitre(), sheet.getCode(), "Sans titre");
        return "La fiche \"" + label + "\" est en brouillon et peut être complétée.";
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }
}
