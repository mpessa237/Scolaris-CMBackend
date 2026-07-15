package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.SchoolYearRequest;
import com.example.Scolaris_CM.dtos.SchoolYearResponse;
import com.example.Scolaris_CM.models.SchoolYear;
import com.example.Scolaris_CM.repository.SchoolYearRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolYearServiceTest {

    @Mock
    private SchoolYearRepo schoolYearRepo;

    @InjectMocks
    private SchoolYearService schoolYearService;

    private SchoolYearRequest schoolYearRequest;

    @BeforeEach
    void setUp() {
        schoolYearRequest = new SchoolYearRequest();
        schoolYearRequest.setLabel("2025-2026");
        schoolYearRequest.setStartDate(LocalDate.of(2025, 9, 1));
        schoolYearRequest.setEndDate(LocalDate.of(2026, 6, 30));
    }

    @Test
    void create_devraitCreerAnneeScolaire_quandDonneesValides() {
        // Arrange
        when(schoolYearRepo.existsByLabel("2025-2026")).thenReturn(false);

        // On simule le save() en renvoyant l'objet avec un id généré
        when(schoolYearRepo.save(any(SchoolYear.class))).thenAnswer(invocation -> {
            SchoolYear saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        // Act
        SchoolYearResponse response = schoolYearService.create(schoolYearRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getLabel()).isEqualTo("2025-2026");
        assertThat(response.getStartDate()).isEqualTo(LocalDate.of(2025, 9, 1));
        assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2026, 6, 30));
    }
}