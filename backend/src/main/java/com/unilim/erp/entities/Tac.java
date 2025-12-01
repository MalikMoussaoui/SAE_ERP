package com.unilim.erp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tac_entry")
@Getter
@Setter
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

    public Tac() {
    }

    public Tac(
            Department department,
            LocalDate date,
            String observation,
            String origin,
            String thematic,
            String correction,
            String analyseCauses,
            String action,
            LocalDate dueDate,
            String status,
            String commentary
    ) {
        this.department = department;
        this.date = date;
        this.observation = observation;
        this.origin = origin;
        this.thematic = thematic;
        this.correction = correction;
        this.analyseCauses = analyseCauses;
        this.action = action;
        this.dueDate = dueDate;
        this.status = status;
        this.commentary = commentary;
    }
}
