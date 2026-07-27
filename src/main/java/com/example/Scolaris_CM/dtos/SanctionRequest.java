package com.example.Scolaris_CM.dtos;

import com.example.Scolaris_CM.models.SanctionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanctionRequest {
    private SanctionType sanctionType ;
    private String description;
    private LocalDate date;
    private Long studentId;
}