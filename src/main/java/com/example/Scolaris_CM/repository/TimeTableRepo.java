package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeTableRepo extends JpaRepository<Timetable,Long> {

    // Tous les créneaux d'un enseignant sur un jour donné (pour vérifier les chevauchements)
    List<Timetable> findByTeacherIdAndDayOfWeek(Long teacherId, String dayOfWeek);

    // Tous les créneaux d'une classe sur un jour donné (pour vérifier les chevauchements)
    List<Timetable> findBySchoolClassIdAndDayOfWeek(Long schoolClassId, String dayOfWeek);

    List<Timetable> findBySchoolClassId(Long schoolClassId);

    List<Timetable> findByTeacherId(Long teacherId);}
