package com.unilim.erp.entities;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

// --- IMPORT CORRIGÉ ---
// PAS org.hibernate.mapping.Set
import java.util.Set;
// ---

@Getter
@Setter
@Entity
@Table(name = "competency")
public class Competency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ManyToMany(mappedBy = "competencies", fetch = FetchType.LAZY)
    private Set<ResourceSheet> resourceSheets = new LinkedHashSet<>();

    public Competency() {
    }

    public Competency(UUID id, String title) {
        this.id = id;
        this.title = title;
    }
}
