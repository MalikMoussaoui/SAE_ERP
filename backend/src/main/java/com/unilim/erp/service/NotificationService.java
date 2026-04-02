package com.unilim.erp.service;

import com.unilim.erp.dto.NotificationDTO;
import com.unilim.erp.entities.AppUser;
import com.unilim.erp.repositories.ResourceSheetRepository;
import com.unilim.erp.domain.ResourceSheetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ResourceSheetRepository resourceSheetRepository;

    @Transactional(readOnly = true)
    public List<NotificationDTO> getPendingNotificationsForUser(AppUser user) {
        List<NotificationDTO> notifications = new ArrayList<>();

        if ("ROLE_DIRECTOR".equals(user.getRole().getName())) {
             resourceSheetRepository.findByStatus(ResourceSheetStatus.PENDING).forEach(sheet -> {
                 notifications.add(NotificationDTO.builder()
                         .type("RESOURCE_SHEET")
                         .message("La fiche ressource " + sheet.getName() + " est en attente de validation.")
                         .actionUrl("/resources/" + sheet.getId())
                         .urgency("HIGH")
                         .entityId(sheet.getId())
                         .build());
             });
        }

        return notifications;
    }
}