package com.example.Scolaris_CM.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "school_classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ex: "6eme A", "3eme B"
    @Column(nullable = false)
    private String name;

    // Ex: "6eme", "5eme", "4eme", "3eme"
    @Column(nullable = false)
    private String level;

    private int maxCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_year_id", nullable = false)
    private SchoolYear schoolYear;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    private List<Student> students;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    private List<Timetable> timetableSlots;
}
