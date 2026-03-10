package com.unilim.erp.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

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
    @JsonAlias({ "pedagogicalFeedback" })
    private String retourEquipePedagogique;
    private String typeEnseignement;
    private String sequencesRowsJson;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isValidated;

    public ResourceSheetDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUe() {
        return ue;
    }

    public void setUe(String ue) {
        this.ue = ue;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public double getHCM() {
        return hCM;
    }

    public void setHCM(double hCM) {
        this.hCM = hCM;
    }

    public double getHTD() {
        return hTD;
    }

    public void setHTD(double hTD) {
        this.hTD = hTD;
    }

    public double getHTP() {
        return hTP;
    }

    public void setHTP(double hTP) {
        this.hTP = hTP;
    }

    public String getTypeEvaluation() {
        return typeEvaluation;
    }

    public void setTypeEvaluation(String typeEvaluation) {
        this.typeEvaluation = typeEvaluation;
    }

    public double getCoefficientRessource() {
        return coefficientRessource;
    }

    public void setCoefficientRessource(double coefficientRessource) {
        this.coefficientRessource = coefficientRessource;
    }

    public String getEvaluationsPrevues() {
        return evaluationsPrevues;
    }

    public void setEvaluationsPrevues(String evaluationsPrevues) {
        this.evaluationsPrevues = evaluationsPrevues;
    }

    public double getNoteMinimale() {
        return noteMinimale;
    }

    public void setNoteMinimale(double noteMinimale) {
        this.noteMinimale = noteMinimale;
    }

    public String getCompensation() {
        return compensation;
    }

    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }

    public String getRattrapage() {
        return rattrapage;
    }

    public void setRattrapage(String rattrapage) {
        this.rattrapage = rattrapage;
    }

    public String getModaliteRattrapage() {
        return modaliteRattrapage;
    }

    public void setModaliteRattrapage(String modaliteRattrapage) {
        this.modaliteRattrapage = modaliteRattrapage;
    }

    public String getResponsablePedagogique() {
        return responsablePedagogique;
    }

    public void setResponsablePedagogique(String responsablePedagogique) {
        this.responsablePedagogique = responsablePedagogique;
    }

    public String getIntervenants() {
        return intervenants;
    }

    public void setIntervenants(String intervenants) {
        this.intervenants = intervenants;
    }

    public String getRetourEquipePedagogique() {
        return retourEquipePedagogique;
    }

    public void setRetourEquipePedagogique(String retourEquipePedagogique) {
        this.retourEquipePedagogique = retourEquipePedagogique;
    }

    public String getTypeEnseignement() {
        return typeEnseignement;
    }

    public void setTypeEnseignement(String typeEnseignement) {
        this.typeEnseignement = typeEnseignement;
    }

    public String getSequencesRowsJson() {
        return sequencesRowsJson;
    }

    public void setSequencesRowsJson(String sequencesRowsJson) {
        this.sequencesRowsJson = sequencesRowsJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }
}
