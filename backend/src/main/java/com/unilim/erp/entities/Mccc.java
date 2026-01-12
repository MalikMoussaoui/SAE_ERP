package com.unilim.erp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mccc")
@Getter
@Setter
public class Mccc {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String department;
    private String ue;
    private String years;
    private String semester;

    @Column(columnDefinition = "TEXT")
    private String form;

    @Column(name = "ressources_rows", columnDefinition = "TEXT")
    private String ressourcesRows;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt = LocalDateTime.now();

    @PreUpdate
    @PrePersist
    public void updateTimestamps() {
        savedAt = LocalDateTime.now();
    }
}