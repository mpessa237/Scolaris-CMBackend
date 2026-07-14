package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.SchoolClassRequest;
import com.example.Scolaris_CM.dtos.SchoolClassResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.SchoolClass;
import com.example.Scolaris_CM.models.SchoolYear;
import com.example.Scolaris_CM.repository.SchoolClassRepo;
import com.example.Scolaris_CM.repository.SchoolYearRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepo schoolClassRepo;
    private final SchoolYearRepo schoolYearRepo;

    public SchoolClassResponse create(SchoolClassRequest schoolClassRequest) {
        SchoolYear schoolYear = findSchoolYearById(schoolClassRequest.getSchoolYearId());

        if (schoolClassRepo.existsByNameAndSchoolYearId(schoolClassRequest.getName(), schoolClassRequest.getSchoolYearId())) {
            throw new IllegalArgumentException(
                    "Une classe nommée '" + schoolClassRequest.getName() + "' existe déjà pour cette année scolaire");
        }

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(schoolClassRequest.getName());
        schoolClass.setLevel(schoolClassRequest.getLevel());
        schoolClass.setMaxCapacity(schoolClassRequest.getMaxCapacity());
        schoolClass.setSchoolYear(schoolYear);

        return toResponse(schoolClassRepo.save(schoolClass));
    }

    public List<SchoolClassResponse> getAll() {
        return schoolClassRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SchoolClassResponse> getBySchoolYear(Long schoolYearId) {
        return schoolClassRepo.findBySchoolYearId(schoolYearId).stream()
                .map(this::toResponse)
                .toList();
    }

    public SchoolClassResponse getById(Long id) {
        return toResponse(findEntityById(id));
    }

    public SchoolClassResponse update(Long id, SchoolClassRequest request) {
        SchoolClass schoolClass = findEntityById(id);
        SchoolYear schoolYear = findSchoolYearById(request.getSchoolYearId());

        boolean nameChanged = !schoolClass.getName().equals(request.getName());
        boolean yearChanged = !schoolClass.getSchoolYear().getId().equals(request.getSchoolYearId());
        if ((nameChanged || yearChanged)
                && schoolClassRepo.existsByNameAndSchoolYearId(request.getName(), request.getSchoolYearId())) {
            throw new IllegalArgumentException(
                    "Une classe nommée '" + request.getName() + "' existe déjà pour cette année scolaire");
        }

        schoolClass.setName(request.getName());
        schoolClass.setLevel(request.getLevel());
        schoolClass.setMaxCapacity(request.getMaxCapacity());
        schoolClass.setSchoolYear(schoolYear);

        return toResponse(schoolClassRepo.save(schoolClass));
    }

    public void delete(Long id) {
        SchoolClass schoolClass = findEntityById(id);
        schoolClassRepo.delete(schoolClass);
    }

    private SchoolClass findEntityById(Long id) {
        return schoolClassRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe introuvable avec l'id : " + id));
    }

    private SchoolYear findSchoolYearById(Long id) {
        return schoolYearRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année scolaire introuvable avec l'id : " + id));
    }

    private SchoolClassResponse toResponse(SchoolClass schoolClass) {
        return new SchoolClassResponse(
                schoolClass.getId(),
                schoolClass.getName(),
                schoolClass.getLevel(),
                schoolClass.getMaxCapacity(),
                schoolClass.getSchoolYear().getId(),
                schoolClass.getSchoolYear().getLabel()
        );
    }
}
