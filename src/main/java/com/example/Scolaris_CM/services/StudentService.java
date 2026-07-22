package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.StudentRequest;
import com.example.Scolaris_CM.dtos.StudentResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.SchoolClass;
import com.example.Scolaris_CM.models.Student;
import com.example.Scolaris_CM.repository.SchoolClassRepo;
import com.example.Scolaris_CM.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepo studentRepo;
    private final SchoolClassRepo schoolClassRepo;

    public StudentResponse create(StudentRequest request) {

        SchoolClass schoolClass = schoolClassRepo.findById(request.getSchoolClassId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classe introuvable avec l'id : " + request.getSchoolClassId()));

        ensureClassHasCapacity(schoolClass); // On ne depasse pas la capicité max de la classe

        Student student = new Student();
        student.setRegistrationNumber(generateRegistrationNumber());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setGuardianName(request.getGuardianName());
        student.setGuardianPhone(request.getGuardianPhone());
        student.setSchoolClass(schoolClass);

        return toResponse(studentRepo.save(student));
    }

    public List<StudentResponse> getAll() {
        return studentRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<StudentResponse> getBySchoolClass(Long schoolClassId) {
        return studentRepo.findBySchoolClassId(schoolClassId).stream()
                .map(this::toResponse)
                .toList();
    }

    public StudentResponse getById(Long id) {
        return toResponse(findEntityById(id));
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findEntityById(id);

        // Le matricule est généré une seule fois à la création, il ne change jamais ensuite
        if (!student.getSchoolClass().getId().equals(request.getSchoolClassId())) {
            SchoolClass newClass = schoolClassRepo.findById(request.getSchoolClassId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Classe introuvable avec l'id : " + request.getSchoolClassId()));

            ensureClassHasCapacity(newClass);
            student.setSchoolClass(newClass);
        }

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setGuardianName(request.getGuardianName());
        student.setGuardianPhone(request.getGuardianPhone());

        return toResponse(studentRepo.save(student));
    }

    public void delete(Long id) {
        Student student = findEntityById(id);
        studentRepo.delete(student);
    }

    // Se base sur le DERNIER matricule existant (pas sur un simple comptage) pour éviter
    // toute collision après suppression d'un élève en cours d'année.
    private String generateRegistrationNumber() {
        String yearPrefix = String.valueOf(LocalDate.now().getYear());

        int nextNumber = studentRepo
                .findTopByRegistrationNumberStartingWithOrderByRegistrationNumberDesc(yearPrefix)
                .map(this::extractSequenceNumber)
                .map(n -> n + 1)
                .orElse(1);

        return yearPrefix + "-" + String.format("%04d", nextNumber);
    }

    private int extractSequenceNumber(Student student) {
        // Format attendu : "2026-0007" -> on extrait "0007"
        String registrationNumber = student.getRegistrationNumber();
        String suffix = registrationNumber.substring(registrationNumber.indexOf('-') + 1);
        return Integer.parseInt(suffix);
    }

    private void ensureClassHasCapacity(SchoolClass schoolClass) {
        long currentCount = studentRepo.findBySchoolClassId(schoolClass.getId()).size();
        if (currentCount >= schoolClass.getMaxCapacity()) {
            throw new IllegalArgumentException(
                    "La classe " + schoolClass.getName() + " a atteint sa capacité maximale ("
                            + schoolClass.getMaxCapacity() + " élèves)");
        }
    }

    private Student findEntityById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Élève introuvable avec l'id : " + id));
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getRegistrationNumber(),
                student.getFirstName(),
                student.getLastName(),
                student.getDateOfBirth(),
                student.getGender(),
                student.getGuardianName(),
                student.getGuardianPhone(),
                student.getSchoolClass().getId(),
                student.getSchoolClass().getName()
        );
    }
}