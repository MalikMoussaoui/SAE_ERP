package com.unilim.erp.entities;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "resource")
@Setter
@Getter
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ue_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Ue ue;

    @Column(nullable = false)
    private String title;

    @Column(name = "hours_cm")
    private int cmHours;

    @Column(name = "hours_td")
    private int tdHours;

    @Column(name = "hours_tp")
    private int tpHours;

    public Resource() {
    }

    public Resource(UUID id, Ue ue, String title, int cmHours, int tdHours, int tpHours) {
        this.id = id;
        this.ue = ue;
        this.title = title;
        this.cmHours = cmHours;
        this.tdHours = tdHours;
        this.tpHours = tpHours;
    }
}
