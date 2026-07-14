package com.example.Scolaris_CM.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolYearResponse {
    private Long id;
    private String label;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
}
