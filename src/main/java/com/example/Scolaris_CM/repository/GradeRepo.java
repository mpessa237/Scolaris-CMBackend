package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepo extends JpaRepository<Grade, Long> {



    boolean existsByStudentIdAndSubjectIdAndSequenceId(Long studentId, Long subjectId, Long sequenceId);

    // Utile plus tard pour calculer la moyenne d'un élève sur une séquence donnée
    List<Grade> findByStudentIdAndSequenceId(Long studentId, Long sequenceId);

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findBySequenceId(Long sequenceId);
}