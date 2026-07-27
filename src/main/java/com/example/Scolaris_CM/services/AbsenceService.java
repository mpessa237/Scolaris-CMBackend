package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.AbsenceRequest;
import com.example.Scolaris_CM.dtos.AbsenceResponse;
import com.example.Scolaris_CM.dtos.SanctionResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.Absence;
import com.example.Scolaris_CM.models.Role;
import com.example.Scolaris_CM.models.Student;
import com.example.Scolaris_CM.models.User;
import com.example.Scolaris_CM.repository.AbsenceRepo;
import com.example.Scolaris_CM.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceRepo absenceRepo;
    private final StudentRepo studentRepo;

    public AbsenceResponse create(AbsenceRequest request, User recordedBy) {

        Student student = findStudentById(request.getStudentId());

        Absence absence = new Absence();
        absence.setDate(request.getDate());
        absence.setReason(request.getReason());
        absence.setJustified(request.isJustified());
        absence.setStudent(student);
        absence.setRecordedBy(recordedBy);

        return toResponse(absenceRepo.save(absence));
    }

    public List<AbsenceResponse> getAll() {
        return absenceRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AbsenceResponse getById(Long id) {
        return toResponse(findEntityById(id));
    }

    public List<AbsenceResponse> getByStudent(Long studentId) {
        return absenceRepo.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    // Seul l'auteur (le surveillant qui a enregistré l'absence) ou un ADMIN peut modifier
    public AbsenceResponse update(Long id, AbsenceRequest request, User currentUser) {
        Absence absence = findEntityById(id);
        checkOwnerOrAdmin(absence.getRecordedBy(), currentUser, "modifier");

        Student student = findStudentById(request.getStudentId());

        absence.setDate(request.getDate());
        absence.setReason(request.getReason());
        absence.setJustified(request.isJustified());
        absence.setStudent(student);

        return toResponse(absenceRepo.save(absence));
    }

    public void delete(Long id, User currentUser) {
        Absence absence = findEntityById(id);
        checkOwnerOrAdmin(absence.getRecordedBy(), currentUser, "supprimer");
        absenceRepo.delete(absence);
    }

    private void checkOwnerOrAdmin(User recordedBy, User currentUser, String action) {
        boolean isOwner = recordedBy.getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException(
                    "Seul le surveillant ayant enregistré cette absence peut la " + action);
        }
    }

    private Student findStudentById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Élève introuvable avec l'id : " + id));
    }

    private Absence findEntityById(Long id) {
        return absenceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Absence introuvable avec l'id : " + id));
    }

    private AbsenceResponse toResponse(Absence absence) {
        return new AbsenceResponse(
                absence.getId(),
                absence.getDate(),
                absence.getReason(),
                absence.isJustified(),
                absence.getStudent().getId(),
                absence.getStudent().getFirstName() + " " + absence.getStudent().getLastName(),
                absence.getRecordedBy().getId(),
                absence.getRecordedBy().getFirstName() + " " + absence.getRecordedBy().getLastName()
        );
    }
}