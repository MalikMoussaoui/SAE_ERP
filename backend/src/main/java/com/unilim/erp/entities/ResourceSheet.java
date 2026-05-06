package com.unilim.erp.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resource_sheet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String titre;
    private String code;
    private String semestre;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ue_id")
    private Ue ue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "h_cm")
    private double hCM;

    @Column(name = "h_td")
    private double hTD;

    @Column(name = "h_tp")
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
    @Column(columnDefinition = "TEXT")
    private String retourEquipePedagogique;
    private String typeEnseignement;
    @Column(columnDefinition = "TEXT")
    private String sequencesRowsJson;

    @Column(name = "created_by")
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_submitted", nullable = false)
    private boolean isSubmitted = false;

    @Column(name = "is_validated", nullable = false)
    private boolean isValidated = false;
}
