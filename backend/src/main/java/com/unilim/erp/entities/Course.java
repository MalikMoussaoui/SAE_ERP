package com.unilim.erp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "course")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "hour", nullable = false)
    private int hour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_resource_sheet")
    private ResourceSheet resourceSheet;

    public Course() {
    }

    public Course(int hour, ResourceSheet resourceSheet) {
        this.hour = hour;
        this.resourceSheet = resourceSheet;
    }
}
