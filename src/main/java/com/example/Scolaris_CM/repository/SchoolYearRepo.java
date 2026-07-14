package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolYearRepo extends JpaRepository<SchoolYear,Long> {

    Optional<SchoolYear> findByLabel(String label);

    boolean existsByLabel(String label);

    Optional<SchoolYear> findByActiveTrue();
}
