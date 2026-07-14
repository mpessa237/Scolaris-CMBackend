package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.SubjectRequest;
import com.example.Scolaris_CM.dtos.SubjectResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.Subject;
import com.example.Scolaris_CM.repository.SubjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepo subjectRepo;

    public SubjectResponse create(SubjectRequest subjectRequest) {
        if (subjectRepo.existsByName(subjectRequest.getName())) {
            throw new IllegalArgumentException("Une matière existe déjà avec ce nom : " + subjectRequest.getName());
        }

        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        subject.setCoefficient(subjectRequest.getCoefficient());

        return toResponse(subjectRepo.save(subject));
    }

    public List<SubjectResponse> getAll() {
        return subjectRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public SubjectResponse getById(Long id) {
        return toResponse(findEntityById(id));
    }

    public SubjectResponse update(Long id, SubjectRequest request) {
        Subject subject = findEntityById(id);

        if (!subject.getName().equals(request.getName()) && subjectRepo.existsByName(request.getName())) {
            throw new IllegalArgumentException("Une matière existe déjà avec ce nom : " + request.getName());
        }

        subject.setName(request.getName());
        subject.setCoefficient(request.getCoefficient());

        return toResponse(subjectRepo.save(subject));
    }

    public void delete(Long id) {
        Subject subject = findEntityById(id);
        subjectRepo.delete(subject);
    }

    private Subject findEntityById(Long id) {
        return subjectRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière introuvable avec l'id : " + id));
    }

    private SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getName(),
                subject.getCoefficient());
    }
}
