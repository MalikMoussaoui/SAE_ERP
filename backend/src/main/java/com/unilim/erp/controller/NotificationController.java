package com.unilim.erp.controller;

import com.unilim.erp.dto.NotificationDTO;
import com.unilim.erp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/pending")
    public ResponseEntity<List<NotificationDTO>> getPending(Authentication authentication) {
        List<NotificationDTO> notifications = notificationService.getPendingNotificationsForUser();
        return ResponseEntity.ok(notifications);
    }
}