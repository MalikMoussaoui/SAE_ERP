package com.unilim.erp.entities;

import jakarta.persistence.*;
import java.util.UUID;

import lombok.*;

@Entity
@Table(name = "sae")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sae {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ue_id")
    private Ue ue;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "hours_sae", nullable = false)
    private int hours;

}
