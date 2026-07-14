package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.SchoolYearRequest;
import com.example.Scolaris_CM.dtos.SchoolYearResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.SchoolYear;
import com.example.Scolaris_CM.repository.SchoolYearRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolYearService {

    private final SchoolYearRepo schoolYearRepo;

    public SchoolYearResponse create(SchoolYearRequest schoolYearRequest) {
        if (schoolYearRepo.existsByLabel(schoolYearRequest.getLabel())) {
            throw new IllegalArgumentException("Une année scolaire existe déjà avec ce libellé : " + schoolYearRequest.getLabel());
        }
        if (schoolYearRequest.getEndDate().isBefore(schoolYearRequest.getStartDate())) {
            throw new IllegalArgumentException("La date de fin ne peut pas précéder la date de début");
        }

        SchoolYear schoolYear = new SchoolYear();
        schoolYear.setLabel(schoolYearRequest.getLabel());
        schoolYear.setStartDate(schoolYearRequest.getStartDate());
        schoolYear.setEndDate(schoolYearRequest.getEndDate());
        schoolYear.setActive(false);

        return toResponse(schoolYearRepo.save(schoolYear));
    }

    public List<SchoolYearResponse> getAll() {
        return schoolYearRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public SchoolYearResponse getById(Long id) {
        return toResponse(findEntityById(id));
    }

    public SchoolYearResponse update(Long id, SchoolYearRequest request) {
        SchoolYear schoolYear = findEntityById(id);

        if (!schoolYear.getLabel().equals(request.getLabel())
                && schoolYearRepo.existsByLabel(request.getLabel())) {
            throw new IllegalArgumentException("Une année scolaire existe déjà avec ce libellé : " + request.getLabel());
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("La date de fin ne peut pas précéder la date de début");
        }

        schoolYear.setLabel(request.getLabel());
        schoolYear.setStartDate(request.getStartDate());
        schoolYear.setEndDate(request.getEndDate());

        return toResponse(schoolYearRepo.save(schoolYear));
    }

    public void delete(Long id) {
        SchoolYear schoolYear = findEntityById(id);
        schoolYearRepo.delete(schoolYear);
    }

    // Une seule année scolaire active à la fois : on désactive les autres avant d'activer celle-ci
    @Transactional
    public SchoolYearResponse activate(Long id) {
        SchoolYear toActivate = findEntityById(id);

        schoolYearRepo.findByActiveTrue().ifPresent(current -> {
            current.setActive(false);
            schoolYearRepo.save(current);
        });

        toActivate.setActive(true);
        return toResponse(schoolYearRepo.save(toActivate));
    }

    private SchoolYear findEntityById(Long id) {
        return schoolYearRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année scolaire introuvable avec l'id : " + id));
    }

    private SchoolYearResponse toResponse(SchoolYear schoolYear) {
        return new SchoolYearResponse(
                schoolYear.getId(),
                schoolYear.getLabel(),
                schoolYear.getStartDate(),
                schoolYear.getEndDate(),
                schoolYear.isActive()
        );
    }
}
