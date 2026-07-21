package com.example.Scolaris_CM.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeResponse {
    private Long id;
    private double score;
    private Long studentId;
    private String studentFullName;
    private Long subjectId;
    private String subjectName;
    private Long sequenceId;
    private int sequenceNumber;
    private Long recordedById;
    private String recordedByFullName;
}