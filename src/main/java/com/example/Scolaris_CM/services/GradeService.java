package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.GradeRequest;
import com.example.Scolaris_CM.dtos.GradeResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.*;
import com.example.Scolaris_CM.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepo gradeRepo;
    private final StudentRepo studentRepo;
    private final SubjectRepo subjectRepo;
    private final SequenceRepo sequenceRepo;

    public GradeResponse create(GradeRequest request, User recordedBy) {

        if (request.getScore() < 0 || request.getScore() > 20) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20");
        }

        Student student = studentRepo.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Élève introuvable avec l'id : " + request.getStudentId()));

        Subject subject = subjectRepo.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Matière introuvable avec l'id : " + request.getSubjectId()));

        Sequence sequence = sequenceRepo.findById(request.getSequenceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Séquence introuvable avec l'id : " + request.getSequenceId()));

        if (gradeRepo.existsByStudentIdAndSubjectIdAndSequenceId(
                request.getStudentId(), request.getSubjectId(), request.getSequenceId())) {
            throw new IllegalArgumentException(
                    "Une note existe déjà pour cet élève, cette matière et cette séquence");
        }

        Grade grade = new Grade();
        grade.setScore(request.getScore());
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setSequence(sequence);
        grade.setRecordedBy(recordedBy);

        return toResponse(gradeRepo.save(grade));
    }

    public List<GradeResponse> getByStudent(Long studentId) {
        return gradeRepo.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public GradeResponse update(Long id, GradeRequest request) {
        if (request.getScore() < 0 || request.getScore() > 20) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20");
        }

        Grade grade = gradeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note introuvable avec l'id : " + id));

        grade.setScore(request.getScore());
        return toResponse(gradeRepo.save(grade));
    }

    public void delete(Long id) {
        Grade grade = gradeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note introuvable avec l'id : " + id));
        gradeRepo.delete(grade);
    }

    private GradeResponse toResponse(Grade grade) {
        return new GradeResponse(
                grade.getId(),
                grade.getScore(),
                grade.getStudent().getId(),
                grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName(),
                grade.getSubject().getId(),
                grade.getSubject().getName(),
                grade.getSequence().getId(),
                grade.getSequence().getNumber(),
                grade.getRecordedBy().getId(),
                grade.getRecordedBy().getFirstName() + " " + grade.getRecordedBy().getLastName()
        );
    }
}