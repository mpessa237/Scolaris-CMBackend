package com.example.Scolaris_CM.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableRequest {


    // Valeurs attendues : MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
   // @NotBlank(message = "Le jour de la semaine est obligatoire")
    private String dayOfWeek;

    //@NotNull(message = "L'heure de début est obligatoire")
    private LocalTime startTime;

    //@NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime endTime;

    //@NotNull(message = "La classe est obligatoire")
    private Long schoolClassId;

    //@NotNull(message = "La matière est obligatoire")
    private Long subjectId;

    //@NotNull(message = "L'enseignant est obligatoire")
    private Long teacherId;
}
