package com.unilim.erp.controller;

import com.unilim.erp.dto.DashboardStatsDTO;
import com.unilim.erp.entities.ResourceSheet;
import com.unilim.erp.repositories.AppUserRepository;
import com.unilim.erp.repositories.ResourceSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    // Injecter les repositories pour accéder aux données nécessaires
    private final AppUserRepository userRepository;
    private final ResourceSheetRepository sheetRepository;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() {
        var sheets = sheetRepository.findAll();

        var latestSheet = sheetRepository.findAll().stream()
                .sorted(Comparator.comparing(ResourceSheet::getUpdatedAt).reversed())
                .findFirst()
                .orElse(null);

        // Calcul des heures totales
        Map<String, Double> hours = new HashMap<>();
        hours.put("CM", sheets.stream().mapToDouble(s -> s.getHCM()).sum());
        hours.put("TD", sheets.stream().mapToDouble(s -> s.getHTD()).sum());
        hours.put("TP", sheets.stream().mapToDouble(s -> s.getHTP()).sum());

        // Construire et retourner le DTO avec les statistiques
        return ResponseEntity.ok(DashboardStatsDTO.builder()
                .totalUsers(userRepository.count())
                .totalResourceSheets(sheets.size())
                .hoursByType(hours)
                .usersByRole(userRepository.findAll().stream()
                        .collect(Collectors.groupingBy(u -> u.getRole().name(), Collectors.counting())))
                .latestSheet(latestSheet)
                .build());
    }
}