package com.example.Scolaris_CM.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "sequences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1 a 6
    @Column(nullable = false)
    private int number;

    // 1 = premier trimestre, 2 = deuxieme, 3 = troisieme
    @Column(nullable = false)
    private int term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_year_id", nullable = false)
    private SchoolYear schoolYear;

    @OneToMany(mappedBy = "sequence", cascade = CascadeType.ALL)
    private List<Grade> grades;
}
