package com.unilim.erp.entities;

import com.unilim.erp.domain.UserRole;
import com.unilim.erp.domain.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable=false)
    private String displayName;

    @Column
    private String phone;

    @Column
    private String departement;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserStatus status;

    @Column(nullable = false,updatable = false)
    private Instant createdAt = Instant.now();

    @ManyToMany(mappedBy = "teachers")
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

}