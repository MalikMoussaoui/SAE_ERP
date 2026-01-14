package com.unilim.erp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.UUID;

@Data
public class ResourceSheetDto {
    private UUID id;
    private String titre;
    private String departement;
    private String description;
    private String code;
    private String ue;
    private String semestre;
    @JsonProperty("hCM")
    private double hCM;
    @JsonProperty("hTD")
    private double hTD;
    @JsonProperty("hTP")
    private double hTP;
    private String typeEvaluation;
    private double coefficientRessource;
    private String evaluationsPrevues;
    private double noteMinimale;
    private String compensation;
    private String rattrapage;
    private String modaliteRattrapage;
    private String responsablePedagogique;
    private String intervenants;
    private String typeEnseignement;
    private String sequencesRowsJson;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}