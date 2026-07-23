package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.SanctionRequest;
import com.example.Scolaris_CM.dtos.SanctionResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.Sanction;
import com.example.Scolaris_CM.models.Student;
import com.example.Scolaris_CM.models.User;
import com.example.Scolaris_CM.repository.SanctionRepo;
import com.example.Scolaris_CM.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SanctionService {

    private final SanctionRepo sanctionRepo;
    private final StudentRepo studentRepo;

    public SanctionResponse create(SanctionRequest request, User recordedBy) {

        Student student = studentRepo.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Élève introuvable avec l'id : " + request.getStudentId()));

        Sanction sanction = new Sanction();
        sanction.setType(request.getType());
        sanction.setDescription(request.getDescription());
        sanction.setDate(request.getDate());
        sanction.setStudent(student);
        sanction.setRecordedBy(recordedBy);

        return toResponse(sanctionRepo.save(sanction));
    }

    public List<SanctionResponse> getByStudent(Long studentId) {
        return sanctionRepo.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public SanctionResponse update(Long id, SanctionRequest request) {
        Sanction sanction = sanctionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sanction introuvable avec l'id : " + id));

        sanction.setType(request.getType());
        sanction.setDescription(request.getDescription());
        sanction.setDate(request.getDate());

        return toResponse(sanctionRepo.save(sanction));
    }

    public void delete(Long id) {
        Sanction sanction = sanctionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sanction introuvable avec l'id : " + id));
        sanctionRepo.delete(sanction);
    }

    private SanctionResponse toResponse(Sanction sanction) {
        return new SanctionResponse(
                sanction.getId(),
                sanction.getType(),
                sanction.getDescription(),
                sanction.getDate(),
                sanction.getStudent().getId(),
                sanction.getStudent().getFirstName() + " " + sanction.getStudent().getLastName(),
                sanction.getRecordedBy().getId(),
                sanction.getRecordedBy().getFirstName() + " " + sanction.getRecordedBy().getLastName()
        );
    }
}