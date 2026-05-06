package com.unilim.erp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tac_entry")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tac {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String observation;

    @Column(columnDefinition = "TEXT")
    private String origin;

    @Column(columnDefinition = "TEXT")
    private String thematic;

    @Column(columnDefinition = "TEXT")
    private String correction;

    @Column(name = "analyse_causes", columnDefinition = "TEXT")
    private String analyseCauses;

    @Column(columnDefinition = "TEXT")
    private String action;

    private LocalDate dueDate;

    @Column(columnDefinition = "TEXT")
    private String status;

    @Column(columnDefinition = "TEXT")
    private String commentary;
}
