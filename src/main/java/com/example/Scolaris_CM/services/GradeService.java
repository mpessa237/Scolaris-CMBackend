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

    // currentUser = l'utilisateur authentifié, résolu dans le contrôleur depuis le token JWT.
    // On ne fait jamais confiance à un teacherId envoyé par le client.
    public GradeResponse create(GradeRequest gradeRequest, User currentUser) {

        Student student = findStudentById(gradeRequest.getStudentId());
        Subject subject = findSubjectById(gradeRequest.getSubjectId());
        Sequence sequence = findSequenceById(gradeRequest.getSequenceId());

        if (gradeRepo.existsByStudentIdAndSubjectIdAndSequenceId(
                gradeRequest.getStudentId(), gradeRequest.getSubjectId(), gradeRequest.getSequenceId())) {
            throw new IllegalArgumentException(
                    "Une note existe déjà pour cet élève, cette matière et cette séquence");
        }

        Grade grade = new Grade();
        grade.setScore(gradeRequest.getScore());
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setSequence(sequence);
        grade.setRecordedBy(currentUser);

        return toResponse(gradeRepo.save(grade));
    }

    public List<GradeResponse> getAll() {
        return gradeRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<GradeResponse> getByStudent(Long studentId) {
        return gradeRepo.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<GradeResponse> getByStudentAndSequence(Long studentId, Long sequenceId) {
        return gradeRepo.findByStudentIdAndSequenceId(studentId, sequenceId).stream()
                .map(this::toResponse)
                .toList();
    }

    public GradeResponse getById(Long id) {
        return toResponse(findEntityById(id));
    }

    // Seul l'auteur de la note (ou un ADMIN, vérifié dans le contrôleur) peut la modifier
    public GradeResponse update(Long id, GradeRequest request, User currentUser) {
        Grade grade = findEntityById(id);

        boolean isOwner = grade.getRecordedBy().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("Seul l'enseignant ayant saisi cette note peut la modifier");
        }

        Student student = findStudentById(request.getStudentId());
        Subject subject = findSubjectById(request.getSubjectId());
        Sequence sequence = findSequenceById(request.getSequenceId());

        boolean tripletChanged = !grade.getStudent().getId().equals(request.getStudentId())
                || !grade.getSubject().getId().equals(request.getSubjectId())
                || !grade.getSequence().getId().equals(request.getSequenceId());

        if (tripletChanged && gradeRepo.existsByStudentIdAndSubjectIdAndSequenceId(
                request.getStudentId(), request.getSubjectId(), request.getSequenceId())) {
            throw new IllegalArgumentException(
                    "Une note existe déjà pour cet élève, cette matière et cette séquence");
        }

        grade.setScore(request.getScore());
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setSequence(sequence);

        return toResponse(gradeRepo.save(grade));
    }

    public void delete(Long id, User currentUser) {
        Grade grade = findEntityById(id);

        boolean isOwner = grade.getRecordedBy().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("Seul l'enseignant ayant saisi cette note peut la supprimer");
        }

        gradeRepo.delete(grade);
    }

    private Student findStudentById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Élève introuvable avec l'id : " + id));
    }

    private Subject findSubjectById(Long id) {
        return subjectRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière introuvable avec l'id : " + id));
    }

    private Sequence findSequenceById(Long id) {
        return sequenceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Séquence introuvable avec l'id : " + id));
    }

    private Grade findEntityById(Long id) {
        return gradeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note introuvable avec l'id : " + id));
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