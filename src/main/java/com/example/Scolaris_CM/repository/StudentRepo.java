package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    // Utilisé pour générer le prochain matricule à partir du dernier existant
    Optional<Student> findTopByRegistrationNumberStartingWithOrderByRegistrationNumberDesc(String prefix);

    long countByRegistrationNumberStartingWith(String prefix);

    List<Student> findBySchoolClassId(Long schoolClassId);
}