package com.example.Scolaris_CM.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SequenceRequest {

    private int number;
    private int term;
    private Long schoolYearId;
}
