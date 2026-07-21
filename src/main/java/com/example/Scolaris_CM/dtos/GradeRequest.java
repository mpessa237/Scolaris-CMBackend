package com.example.Scolaris_CM.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeRequest {
    private double score;
    private Long studentId;
    private Long subjectId;
    private Long sequenceId;
}
