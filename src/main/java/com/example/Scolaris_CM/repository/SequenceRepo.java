package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepo extends JpaRepository<Sequence, Long> {
    boolean existsByNumberAndSchoolYearId(int number, Long schoolYearId);
}
