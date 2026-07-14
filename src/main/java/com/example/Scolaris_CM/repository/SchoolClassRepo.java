package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolClassRepo extends JpaRepository<SchoolClass,Long> {

    List<SchoolClass> findBySchoolYearId(Long schoolYearId);

    boolean existsByNameAndSchoolYearId(String name, Long schoolYearId);
}
