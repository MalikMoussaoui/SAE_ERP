package com.unilim.erp.dto;

import com.unilim.erp.entities.ResourceSheet;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

//dto pour les statistiques du dashboard
@Data
@Builder
public class DashboardStatsDTO {
    private long totalUsers; // total des utilisateurs
    private long totalResourceSheets; // total des fiches de ressources
    private Map<String, Double> hoursByType; // CM, TD, TP
    private Map<String, Long> usersByRole; // TEACHER, VACATAIRE, REFERENT, RESPONSABLE_PEDAGOGIQUE, RH, ADMINISTRATEUR
    private ResourceSheet latestSheet;
}

