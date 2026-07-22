package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenceRepo extends JpaRepository<Absence, Long> {

    List<Absence> findByStudentId(Long studentId);
}