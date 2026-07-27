package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.SanctionRequest;
import com.example.Scolaris_CM.dtos.SanctionResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.Role;
import com.example.Scolaris_CM.models.Sanction;
import com.example.Scolaris_CM.models.Student;
import com.example.Scolaris_CM.models.User;
import com.example.Scolaris_CM.repository.SanctionRepo;
import com.example.Scolaris_CM.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanctionService {

    private final SanctionRepo sanctionRepo;
    private final StudentRepo studentRepo;

    public SanctionResponse create(SanctionRequest request, User recordedBy) {

        Student student = findStudentById(request.getStudentId());

        Sanction sanction = new Sanction();
        sanction.setSanctionType(request.getSanctionType());
        sanction.setDescription(request.getDescription());
        sanction.setDate(request.getDate());
        sanction.setStudent(student);
        sanction.setRecordedBy(recordedBy);

        return toResponse(sanctionRepo.save(sanction));
    }

    public List<SanctionResponse> getAll() {
        return sanctionRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public SanctionResponse getById(Long id) {
        return toResponse(findEntityById(id));
    }

    public List<SanctionResponse> getByStudent(Long studentId) {
        return sanctionRepo.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    // Seul l'auteur (le surveillant qui a enregistré la sanction) ou un ADMIN peut modifier
    public SanctionResponse update(Long id, SanctionRequest request, User currentUser) {
        Sanction sanction = findEntityById(id);
        checkOwnerOrAdmin(sanction.getRecordedBy(), currentUser, "modifier");

        Student student = findStudentById(request.getStudentId());

        sanction.setSanctionType(request.getSanctionType());
        sanction.setDescription(request.getDescription());
        sanction.setDate(request.getDate());
        sanction.setStudent(student);

        return toResponse(sanctionRepo.save(sanction));
    }

    public void delete(Long id, User currentUser) {
        Sanction sanction = findEntityById(id);
        checkOwnerOrAdmin(sanction.getRecordedBy(), currentUser, "supprimer");
        sanctionRepo.delete(sanction);
    }

    private void checkOwnerOrAdmin(User recordedBy, User currentUser, String action) {
        boolean isOwner = recordedBy.getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException(
                    "Seul le surveillant ayant enregistré cette sanction peut la " + action);
        }
    }

    private Student findStudentById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Élève introuvable avec l'id : " + id));
    }

    private Sanction findEntityById(Long id) {
        return sanctionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sanction introuvable avec l'id : " + id));
    }

    private SanctionResponse toResponse(Sanction sanction) {
        return new SanctionResponse(
                sanction.getId(),
                sanction.getSanctionType().name(),
                sanction.getDescription(),
                sanction.getDate(),
                sanction.getStudent().getId(),
                sanction.getStudent().getFirstName() + " " + sanction.getStudent().getLastName(),
                sanction.getRecordedBy().getId(),
                sanction.getRecordedBy().getFirstName() + " " + sanction.getRecordedBy().getLastName()
        );
    }
}