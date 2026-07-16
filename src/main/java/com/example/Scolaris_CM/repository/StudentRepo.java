package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {


    long countByRegistrationNumberStartingWith(String prefix);

    List<Student> findBySchoolClassId(Long schoolClassId);
}