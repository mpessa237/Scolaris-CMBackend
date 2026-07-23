package com.example.Scolaris_CM.controllers;

import com.example.Scolaris_CM.dtos.TimeTableRequest;
import com.example.Scolaris_CM.dtos.TimeTableResponse;
import com.example.Scolaris_CM.services.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timetables")
public class TimeTableController {

    private final TimeTableService timeTableService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimeTableResponse> create(@Validated @RequestBody TimeTableRequest timeTableRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timeTableService.create(timeTableRequest));
    }

    @GetMapping
    public ResponseEntity<List<TimeTableResponse>> getAll() {
        return ResponseEntity.ok(timeTableService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeTableResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(timeTableService.getById(id));
    }

    @GetMapping("/by-class/{schoolClassId}")
    public ResponseEntity<List<TimeTableResponse>> getBySchoolClass(@PathVariable Long schoolClassId) {
        return ResponseEntity.ok(timeTableService.getBySchoolClass(schoolClassId));
    }

    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<List<TimeTableResponse>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(timeTableService.getByTeacher(teacherId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimeTableResponse> update(@PathVariable Long id, @Validated @RequestBody TimeTableRequest timeTableRequest) {
        return ResponseEntity.ok(timeTableService.update(id, timeTableRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeTableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
