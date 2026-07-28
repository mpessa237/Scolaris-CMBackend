package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.StudentRequest;
import com.example.Scolaris_CM.dtos.StudentResponse;
import com.example.Scolaris_CM.exceptions.ResourceNotFoundException;
import com.example.Scolaris_CM.models.SchoolClass;
import com.example.Scolaris_CM.models.Student;
import com.example.Scolaris_CM.repository.SchoolClassRepo;
import com.example.Scolaris_CM.repository.StudentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepo studentRepo;

    @Mock
    private SchoolClassRepo schoolClassRepo;

    @InjectMocks
    private StudentService studentService;

    private StudentRequest request;
    private SchoolClass schoolClass;

    @BeforeEach
    void setUp() {
        schoolClass = new SchoolClass();
        schoolClass.setId(1L);
        schoolClass.setName("6eme A");
        schoolClass.setMaxCapacity(30);

        request = new StudentRequest(
                "Paul", "Nkeng",
                LocalDate.of(2012, 3, 15), "M",
                "Mme Nkeng", "699112233", 1L
        );
    }

    @Test
    void create_devraitCreerEleve_quandDonneesValides() {
        when(schoolClassRepo.findById(1L)).thenReturn(Optional.of(schoolClass));
        when(studentRepo.findBySchoolClassId(1L)).thenReturn(Collections.emptyList());
        when(studentRepo.findTopByRegistrationNumberStartingWithOrderByRegistrationNumberDesc(any()))
                .thenReturn(Optional.empty());
        when(studentRepo.save(any(Student.class))).thenAnswer(invocation -> {
            Student saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        StudentResponse response = studentService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getFirstName()).isEqualTo("Paul");
        assertThat(response.getSchoolClassName()).isEqualTo("6eme A");
        assertThat(response.getRegistrationNumber()).contains("-0001");
    }

    @Test
    void create_devraitLeverException_quandClasseIntrouvable() {
        when(schoolClassRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.create(request));
    }

    @Test
    void create_devraitLeverException_quandClassePleine() {
        when(schoolClassRepo.findById(1L)).thenReturn(Optional.of(schoolClass));

        List<Student> classeComplete = Collections.nCopies(30, new Student());
        when(studentRepo.findBySchoolClassId(1L)).thenReturn(classeComplete);

        assertThrows(IllegalArgumentException.class, () -> studentService.create(request));
    }
}