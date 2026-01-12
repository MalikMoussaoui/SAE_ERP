package com.unilim.erp.dto;

import lombok.Data;

@Data
public class ResourceSheetDto {
    private String titre;
    private String departement;
    private String description;
    private double hCM;
    private double hTD;
    private double hTP;
    private String ue;
    private String semestre;
}

