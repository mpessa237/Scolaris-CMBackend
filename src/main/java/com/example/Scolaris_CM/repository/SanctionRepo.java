package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.Sanction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanctionRepo extends JpaRepository<Sanction, Long> {
    List<Sanction> findByStudentId(Long studentId);
}