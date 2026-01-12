package com.unilim.erp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resource_sheet")
@Getter
@Setter
public class ResourceSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "department_name")
    private String departmentName;

    @Column(columnDefinition = "TEXT")
    private String objectives;


    @Column(name = "hours_cm")
    private double hoursCm;

    @Column(name = "hours_td")
    private double hoursTd;

    @Column(name = "hours_tp")
    private double hoursTp;

    private String prerequisites = "Aucun";
    private String modalities = "Présentiel";
    private String status = "DRAFT";
    private int version = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}