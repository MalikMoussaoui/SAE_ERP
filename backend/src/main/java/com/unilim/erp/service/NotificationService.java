package com.unilim.erp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.unilim.erp.dto.NotificationDTO;

@Service
public class NotificationService {

    public List<NotificationDTO> getPendingNotificationsForUser() {
        List<NotificationDTO> notifications = new ArrayList<>();

        // Notification de test pour valider l'affichage Front-End
        // Tu pourras ensuite ajouter tes vraies requêtes SQL ici (ex: tacRepository.findAll())
        notifications.add(NotificationDTO.builder()
                .type("SYSTEME")
                .message("Bienvenue ! Pensez à vérifier vos tâches en attente.")
                .actionUrl("/")
                .urgency("LOW")
                .entityId(1L)
                .build());

        notifications.add(NotificationDTO.builder()
                .type("SAE")
                .message("La SAE 'Conception' nécessite votre attention.")
                .actionUrl("/saes")
                .urgency("HIGH")
                .entityId(2L)
                .build());

        return notifications;
    }
}