package com.example.Scolaris_CM.controllers;

import com.example.Scolaris_CM.dtos.SchoolYearRequest;
import com.example.Scolaris_CM.dtos.SchoolYearResponse;
import com.example.Scolaris_CM.services.SchoolYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/school-y+-ears")
@PreAuthorize("hasRole('ADMIN')")
public class SchoolYearController {

    private final SchoolYearService schoolYearService;

    @PostMapping
    public ResponseEntity<SchoolYearResponse> create(@Validated @RequestBody SchoolYearRequest schoolYearRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolYearService.create(schoolYearRequest));
    }

    @GetMapping
    public ResponseEntity<List<SchoolYearResponse>> getAll() {
        return ResponseEntity.ok(schoolYearService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolYearResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolYearService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolYearResponse> update(@PathVariable Long id, @Validated @RequestBody SchoolYearRequest request) {
        return ResponseEntity.ok(schoolYearService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        schoolYearService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SchoolYearResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(schoolYearService.activate(id));
    }
}
