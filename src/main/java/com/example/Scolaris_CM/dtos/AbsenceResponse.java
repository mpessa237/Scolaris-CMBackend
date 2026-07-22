package com.example.Scolaris_CM.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenceResponse {
    private Long id;
    private LocalDate date;
    private String reason;
    private boolean justified;
    private Long studentId;
    private String studentFullName;
    private Long recordedById;
    private String recordedByFullName;
}