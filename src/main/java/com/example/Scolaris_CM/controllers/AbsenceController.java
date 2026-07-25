package com.example.Scolaris_CM.controllers;

import com.example.Scolaris_CM.dtos.AbsenceRequest;
import com.example.Scolaris_CM.dtos.AbsenceResponse;
import com.example.Scolaris_CM.models.User;
import com.example.Scolaris_CM.services.AbsenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/absences")
public class AbsenceController {

    private final AbsenceService absenceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<AbsenceResponse> create(
            @Validated @RequestBody AbsenceRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(absenceService.create(request, currentUser));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AbsenceResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(absenceService.getByStudent(studentId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<AbsenceResponse> update(@PathVariable Long id, @Validated @RequestBody AbsenceRequest request) {
        return ResponseEntity.ok(absenceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        absenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}