package com.example.Scolaris_CM.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolClassResponse {
    private Long id;
    private String name;
    private String level;
    private int maxCapacity;
    private Long schoolYearId;
    private String schoolYearLabel;
}
