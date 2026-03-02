package com.unilim.erp.entities;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ue")
@Setter
@Getter
public class Ue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Department department;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int semester;

    @Column(columnDefinition = "TEXT")
    private String objectives;

    public Ue() {
    }

    public Ue(Department department, String title, int semester, String objectives) {
        this.department = department;
        this.title = title;
        this.semester = semester;
        this.objectives = objectives;
    }
}
