package com.example.Scolaris_CM.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanctionResponse {
    private Long id;
    private String type;
    private String description;
    private LocalDate date;
    private Long studentId;
    private String studentFullName;
    private Long recordedById;
    private String recordedByFullName;
}