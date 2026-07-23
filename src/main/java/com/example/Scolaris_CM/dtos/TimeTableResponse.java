package com.example.Scolaris_CM.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableResponse {
    private Long id;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long schoolClassId;
    private String schoolClassName;
    private Long subjectId;
    private String subjectName;
    private Long teacherId;
    private String teacherFullName;
}
