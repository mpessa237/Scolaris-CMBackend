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
        SchoolYear schoolYear = findSchoolYearById(request.getSchoolYearId());

        validateNumberMatchesTerm(request.getNumber(), request.getTerm());

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
        return sequenceRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SequenceResponse> getBySchoolYear(Long schoolYearId) {
        return sequenceRepo.findBySchoolYearId(schoolYearId).stream()
                .map(this::toResponse)
                .toList();
    }

    public SequenceResponse getById(Long id) {
        return toResponse(findEntityById(id));
    }

    public SequenceResponse update(Long id, SequenceRequest request) {
        Sequence sequence = findEntityById(id);
        SchoolYear schoolYear = findSchoolYearById(request.getSchoolYearId());

        validateNumberMatchesTerm(request.getNumber(), request.getTerm());

        boolean numberChanged = sequence.getNumber() != request.getNumber();
        boolean yearChanged = !sequence.getSchoolYear().getId().equals(request.getSchoolYearId());
        if ((numberChanged || yearChanged)
                && sequenceRepo.existsByNumberAndSchoolYearId(request.getNumber(), request.getSchoolYearId())) {
            throw new IllegalArgumentException(
                    "La séquence " + request.getNumber() + " existe déjà pour cette année scolaire");
        }

        sequence.setNumber(request.getNumber());
        sequence.setTerm(request.getTerm());
        sequence.setSchoolYear(schoolYear);

        return toResponse(sequenceRepo.save(sequence));
    }

    public void delete(Long id) {
        Sequence sequence = findEntityById(id);
        sequenceRepo.delete(sequence);
    }

    // Règle du système camerounais : séquences 1-2 -> trimestre 1, séquences 3-4 -> trimestre 2, séquences 5-6 -> trimestre 3
    private void validateNumberMatchesTerm(int number, int term) {
        int expectedTerm = ((number - 1) / 2) + 1;
        if (expectedTerm != term) {
            throw new IllegalArgumentException(
                    "La séquence " + number + " doit appartenir au trimestre " + expectedTerm
                            + ", pas au trimestre " + term);
        }
    }

    private SchoolYear findSchoolYearById(Long id) {
        return schoolYearRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année scolaire introuvable avec l'id : " + id));
    }

    private Sequence findEntityById(Long id) {
        return sequenceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Séquence introuvable avec l'id : " + id));
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