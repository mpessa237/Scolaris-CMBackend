package com.example.Scolaris_CM.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "school_years")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ex: "2025-2026"
    @Column(nullable = false, unique = true)
    private String label;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    //@Builder.Default
    private boolean active = false;

    @OneToMany(mappedBy = "schoolYear", cascade = CascadeType.ALL)
    private List<SchoolClass> schoolClasses;

    @OneToMany(mappedBy = "schoolYear", cascade = CascadeType.ALL)
    private List<Sequence> sequences;
}
