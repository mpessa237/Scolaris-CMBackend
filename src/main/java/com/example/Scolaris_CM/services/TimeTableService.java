package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.TimeTableRequest;
import com.example.Scolaris_CM.dtos.TimeTableResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.*;
import com.example.Scolaris_CM.repository.SchoolClassRepo;
import com.example.Scolaris_CM.repository.SubjectRepo;
import com.example.Scolaris_CM.repository.TimeTableRepo;
import com.example.Scolaris_CM.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    // Horaires officiels de l'établissement : 7h30-15h30, sauf le mercredi (matin seulement)
    private static final LocalTime SCHOOL_START = LocalTime.of(7, 30);
    private static final LocalTime SCHOOL_END = LocalTime.of(15, 30);
    private static final LocalTime WEDNESDAY_END = LocalTime.of(12, 30);

    private final TimeTableRepo timeTableRepo ;
    private final SchoolClassRepo schoolClassRepo;
    private final SubjectRepo subjectRepo;
    private final UserRepo userRepo;

    public TimeTableResponse create(TimeTableRequest timeTableRequest) {

        validateTimeRange(timeTableRequest);
        validateSchoolHours(timeTableRequest);

        SchoolClass schoolClass = findSchoolClassById(timeTableRequest.getSchoolClassId());
        Subject subject = findSubjectById(timeTableRequest.getSubjectId());
        User teacher = findTeacherById(timeTableRequest.getTeacherId());

        checkTeacherAvailability(teacher.getId(), timeTableRequest.getDayOfWeek(),
                timeTableRequest.getStartTime(), timeTableRequest.getEndTime(), null);
        checkClassAvailability(schoolClass.getId(), timeTableRequest.getDayOfWeek(),
                timeTableRequest.getStartTime(), timeTableRequest.getEndTime(), null);

        Timetable timetable = new Timetable();
        timetable.setDayOfWeek(timeTableRequest.getDayOfWeek());
        timetable.setStartTime(timeTableRequest.getStartTime());
        timetable.setEndTime(timeTableRequest.getEndTime());
        timetable.setSchoolClass(schoolClass);
        timetable.setSubject(subject);
        timetable.setTeacher(teacher);

        return toResponse(timeTableRepo.save(timetable));
    }

    public List<TimeTableResponse> getAll() {
        return timeTableRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TimeTableResponse> getBySchoolClass(Long schoolClassId) {
        return timeTableRepo.findBySchoolClassId(schoolClassId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TimeTableResponse> getByTeacher(Long teacherId) {
        return timeTableRepo.findByTeacherId(teacherId).stream()
                .map(this::toResponse)
                .toList();
    }

    public TimeTableResponse getById(Long id) {
        return toResponse(findEntityById(id));
    }

    public TimeTableResponse update(Long id, TimeTableRequest timeTableRequest ) {
        Timetable timetable = findEntityById(id);

        validateTimeRange(timeTableRequest);
        validateSchoolHours(timeTableRequest);

        SchoolClass schoolClass = findSchoolClassById(timeTableRequest.getSchoolClassId());
        Subject subject = findSubjectById(timeTableRequest.getSubjectId());
        User teacher = findTeacherById(timeTableRequest.getTeacherId());

        // On exclut le créneau courant (id) de la vérification, sinon il se chevaucherait toujours avec lui-même
        checkTeacherAvailability(teacher.getId(), timeTableRequest.getDayOfWeek(),
                timeTableRequest.getStartTime(), timeTableRequest.getEndTime(), id);
        checkClassAvailability(schoolClass.getId(), timeTableRequest.getDayOfWeek(),
                timeTableRequest.getStartTime(), timeTableRequest.getEndTime(), id);

        timetable.setDayOfWeek(timeTableRequest.getDayOfWeek());
        timetable.setStartTime(timeTableRequest.getStartTime());
        timetable.setEndTime(timeTableRequest.getEndTime());
        timetable.setSchoolClass(schoolClass);
        timetable.setSubject(subject);
        timetable.setTeacher(teacher);

        return toResponse(timeTableRepo.save(timetable));
    }

    public void delete(Long id) {
        Timetable timetable = findEntityById(id);
        timeTableRepo.delete(timetable);
    }

    private void validateTimeRange(TimeTableRequest timeTableRequest) {
        if (!timeTableRequest.getStartTime().isBefore(timeTableRequest.getEndTime())) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin");
        }
    }

    // Horaires officiels : 7h30-15h30 du lundi au vendredi, sauf le mercredi qui s'arrête à 12h30
    private void validateSchoolHours(TimeTableRequest timeTableRequest) {
        LocalTime start = timeTableRequest.getStartTime();
        LocalTime end = timeTableRequest.getEndTime();
        boolean isWednesday = "WEDNESDAY".equalsIgnoreCase(timeTableRequest.getDayOfWeek());
        LocalTime maxEnd = isWednesday ? WEDNESDAY_END : SCHOOL_END;

        if (start.isBefore(SCHOOL_START)) {
            throw new IllegalArgumentException(
                    "Les cours ne peuvent pas commencer avant " + SCHOOL_START);
        }
        if (end.isAfter(maxEnd)) {
            if (isWednesday) {
                throw new IllegalArgumentException(
                        "Le mercredi, les cours doivent se terminer au plus tard à " + WEDNESDAY_END);
            }
            throw new IllegalArgumentException(
                    "Les cours ne peuvent pas se terminer après " + SCHOOL_END);
        }
    }

    // Un enseignant ne peut pas avoir deux créneaux qui se chevauchent le même jour
    private void checkTeacherAvailability(Long teacherId, String dayOfWeek,
                                          java.time.LocalTime startTime, java.time.LocalTime endTime,
                                          Long excludeTimetableId) {
        List<Timetable> existingSlots = timeTableRepo.findByTeacherIdAndDayOfWeek(teacherId, dayOfWeek);
        boolean hasOverlap = existingSlots.stream()
                .filter(slot -> !slot.getId().equals(excludeTimetableId))
                .anyMatch(slot -> overlaps(startTime, endTime, slot.getStartTime(), slot.getEndTime()));

        if (hasOverlap) {
            throw new IllegalArgumentException(
                    "Cet enseignant a déjà un cours sur ce créneau horaire (" + dayOfWeek + ")");
        }
    }

    // Une classe ne peut pas avoir deux cours qui se chevauchent le même jour
    private void checkClassAvailability(Long schoolClassId, String dayOfWeek,
                                        java.time.LocalTime startTime, java.time.LocalTime endTime,
                                        Long excludeTimetableId) {
        List<Timetable> existingSlots = timeTableRepo.findBySchoolClassIdAndDayOfWeek(schoolClassId, dayOfWeek);
        boolean hasOverlap = existingSlots.stream()
                .filter(slot -> !slot.getId().equals(excludeTimetableId))
                .anyMatch(slot -> overlaps(startTime, endTime, slot.getStartTime(), slot.getEndTime()));

        if (hasOverlap) {
            throw new IllegalArgumentException(
                    "Cette classe a déjà un cours sur ce créneau horaire (" + dayOfWeek + ")");
        }
    }

    // Deux intervalles [startA, endA) et [startB, endB) se chevauchent si startA < endB ET startB < endA
    private boolean overlaps(java.time.LocalTime startA, java.time.LocalTime endA,
                             java.time.LocalTime startB, java.time.LocalTime endB) {
        return startA.isBefore(endB) && startB.isBefore(endA);
    }

    private SchoolClass findSchoolClassById(Long id) {
        return schoolClassRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe introuvable avec l'id : " + id));
    }

    private Subject findSubjectById(Long id) {
        return subjectRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière introuvable avec l'id : " + id));
    }

    // Vérifie que l'utilisateur existe ET qu'il a bien le rôle TEACHER
    private User findTeacherById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id : " + id));

        if (user.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException(
                    "L'utilisateur " + user.getEmail() + " n'a pas le rôle TEACHER (rôle actuel : " + user.getRole() + ")");
        }
        return user;
    }

    private Timetable findEntityById(Long id) {
        return timeTableRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Créneau introuvable avec l'id : " + id));
    }

    private TimeTableResponse toResponse(Timetable timetable) {
        return new TimeTableResponse(
                timetable.getId(),
                timetable.getDayOfWeek(),
                timetable.getStartTime(),
                timetable.getEndTime(),
                timetable.getSchoolClass().getId(),
                timetable.getSchoolClass().getName(),
                timetable.getSubject().getId(),
                timetable.getSubject().getName(),
                timetable.getTeacher().getId(),
                timetable.getTeacher().getFirstName() + " " + timetable.getTeacher().getLastName()
        );
    }
}
