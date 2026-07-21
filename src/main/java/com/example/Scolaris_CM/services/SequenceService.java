package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.SequenceRequest;
import com.example.Scolaris_CM.dtos.SequenceResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.SchoolYear;
import com.example.Scolaris_CM.models.Sequence;
import com.example.Scolaris_CM.repository.SchoolYearRepo;
import com.example.Scolaris_CM.repository.SequenceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SequenceService {

    private final SequenceRepo sequenceRepo;
    private final SchoolYearRepo schoolYearRepo;

    public SequenceResponse create(SequenceRequest request) {
        SchoolYear schoolYear = schoolYearRepo.findById(request.getSchoolYearId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Année scolaire introuvable avec l'id : " + request.getSchoolYearId()));

        if (sequenceRepo.existsByNumberAndSchoolYearId(request.getNumber(), request.getSchoolYearId())) {
            throw new IllegalArgumentException(
                    "La séquence " + request.getNumber() + " existe déjà pour cette année scolaire");
        }

        Sequence sequence = new Sequence();
        sequence.setNumber(request.getNumber());
        sequence.setTerm(request.getTerm());
        sequence.setSchoolYear(schoolYear);

        return toResponse(sequenceRepo.save(sequence));
    }

    public List<SequenceResponse> getAll() {
        return sequenceRepo.findAll().stream().map(this::toResponse).toList();
    }

    private SequenceResponse toResponse(Sequence sequence) {
        return new SequenceResponse(
                sequence.getId(),
                sequence.getNumber(),
                sequence.getTerm(),
                sequence.getSchoolYear().getId(),
                sequence.getSchoolYear().getLabel()
        );
    }
}