package com.example.Scolaris_CM.controllers;

import com.example.Scolaris_CM.dtos.SchoolClassRequest;
import com.example.Scolaris_CM.dtos.SchoolClassResponse;
import com.example.Scolaris_CM.services.SchoolClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/school-classes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    @PostMapping
    public ResponseEntity<SchoolClassResponse> create(@Validated @RequestBody SchoolClassRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolClassService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<SchoolClassResponse>> getAll(
            @RequestParam(required = false) Long schoolYearId
    ) {
        if (schoolYearId != null) {
            return ResponseEntity.ok(schoolClassService.getBySchoolYear(schoolYearId));
        }
        return ResponseEntity.ok(schoolClassService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolClassResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolClassService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolClassResponse> update(@PathVariable Long id, @Validated @RequestBody SchoolClassRequest request) {
        return ResponseEntity.ok(schoolClassService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        schoolClassService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
