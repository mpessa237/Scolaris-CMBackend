package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepo extends JpaRepository<Subject,Long> {
    boolean existsByName(String name);

}
