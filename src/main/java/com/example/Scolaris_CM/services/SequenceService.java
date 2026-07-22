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
        if (request.getNumber() < 1 || request.getNumber() > 6) {
            throw new IllegalArgumentException("Le numéro de séquence doit être compris entre 1 et 6");
        }
        if (request.getTerm() < 1 || request.getTerm() > 3) {
            throw new IllegalArgumentException("Le trimestre doit être 1, 2 ou 3");
        }
        // Validation croisée séquence/trimestre
        if ((request.getTerm() == 1 && (request.getNumber() < 1 || request.getNumber() > 2)) ||
                (request.getTerm() == 2 && (request.getNumber() < 3 || request.getNumber() > 4)) ||
                (request.getTerm() == 3 && (request.getNumber() < 5 || request.getNumber() > 6))) {
            throw new IllegalArgumentException(
                    "La séquence " + request.getNumber() + " n'est pas valide pour le trimestre " + request.getTerm());
        }

        // 2. Vérification de l'année scolaire
        SchoolYear schoolYear = schoolYearRepo.findById(request.getSchoolYearId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Année scolaire introuvable avec l'id : " + request.getSchoolYearId()));

        // 3. Vérification d'unicité (séquence + trimestre + année)
        if (sequenceRepo.existsByNumberAndTermAndSchoolYearId(
                request.getNumber(), request.getTerm(), request.getSchoolYearId())) {
            throw new IllegalArgumentException(
                    "La séquence " + request.getNumber() + " existe déjà pour ce trimestre et cette année scolaire");
        }

        // 4. Création de l'entité
        Sequence sequence = new Sequence();
        sequence.setNumber(request.getNumber());
        sequence.setTerm(request.getTerm());
        sequence.setSchoolYear(schoolYear);

        // 5. Sauvegarde et retour
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