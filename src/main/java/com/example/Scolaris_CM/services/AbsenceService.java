package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.AbsenceRequest;
import com.example.Scolaris_CM.dtos.AbsenceResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.Absence;
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

        Student student = studentRepo.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Élève introuvable avec l'id : " + request.getStudentId()));

        Absence absence = new Absence();
        absence.setDate(request.getDate());
        absence.setReason(request.getReason());
        absence.setJustified(request.isJustified());
        absence.setStudent(student);
        absence.setRecordedBy(recordedBy);

        return toResponse(absenceRepo.save(absence));
    }

    public List<AbsenceResponse> getByStudent(Long studentId) {
        return absenceRepo.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public AbsenceResponse update(Long id, AbsenceRequest request) {
        Absence absence = absenceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Absence introuvable avec l'id : " + id));

        absence.setDate(request.getDate());
        absence.setReason(request.getReason());
        absence.setJustified(request.isJustified());

        return toResponse(absenceRepo.save(absence));
    }

    public void delete(Long id) {
        Absence absence = absenceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Absence introuvable avec l'id : " + id));
        absenceRepo.delete(absence);
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