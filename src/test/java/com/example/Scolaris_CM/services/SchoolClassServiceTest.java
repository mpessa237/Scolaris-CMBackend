package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.SchoolClassRequest;
import com.example.Scolaris_CM.dtos.SchoolClassResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.SchoolClass;
import com.example.Scolaris_CM.models.SchoolYear;
import com.example.Scolaris_CM.repository.SchoolClassRepo;
import com.example.Scolaris_CM.repository.SchoolYearRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolClassServiceTest {

    @Mock
    private SchoolClassRepo schoolClassRepo;

    @Mock
    private SchoolYearRepo schoolYearRepo;

    @InjectMocks
    private SchoolClassService schoolClassService;

    private SchoolClassRequest request;
    private SchoolYear schoolYear;

    @BeforeEach
    void setUp() {
        schoolYear = new SchoolYear();
        schoolYear.setId(1L);
        schoolYear.setLabel("2026-2027");

        request = new SchoolClassRequest("6eme A", "6eme", 30, 1L);
    }

    @Test
    void create_devraitCreerLaClasse_quandDonneesValides() {
        when(schoolYearRepo.findById(1L)).thenReturn(Optional.of(schoolYear));
        when(schoolClassRepo.existsByNameAndSchoolYearId("6eme A", 1L)).thenReturn(false);
        when(schoolClassRepo.save(any(SchoolClass.class))).thenAnswer(invocation -> {
            SchoolClass saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        SchoolClassResponse response = schoolClassService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("6eme A");
        assertThat(response.getSchoolYearLabel()).isEqualTo("2026-2027");
    }

    @Test
    void create_devraitLeverException_quandAnneeScolaireIntrouvable() {
        when(schoolYearRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> schoolClassService.create(request));
    }

    @Test
    void create_devraitLeverException_quandNomDejaUtilisePourCetteAnnee() {
        when(schoolYearRepo.findById(1L)).thenReturn(Optional.of(schoolYear));
        when(schoolClassRepo.existsByNameAndSchoolYearId("6eme A", 1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> schoolClassService.create(request));
    }
}