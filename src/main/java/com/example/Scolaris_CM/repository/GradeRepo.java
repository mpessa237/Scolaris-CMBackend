package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepo extends JpaRepository<Grade, Long> {

    boolean existsByStudentIdAndSubjectIdAndSequenceId(Long studentId, Long subjectId, Long sequenceId);

    List<Grade> findByStudentId(Long studentId);
}