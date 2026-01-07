package com.unilim.erp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "course")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "hours", nullable = false)
    private int hours;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_resource_sheet")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ResourceSheet resourceSheet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sae_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Sae sae;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Resource resource;

    @ManyToMany
    @JoinTable(
            name = "list_prof_course",
            joinColumns = @JoinColumn(name = "id_course"),
            inverseJoinColumns = @JoinColumn(name = "id_prof")
    )
    private Set<AppUser> teachers = new HashSet<>();
}