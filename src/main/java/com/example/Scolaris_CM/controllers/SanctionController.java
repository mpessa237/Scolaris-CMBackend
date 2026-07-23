package com.example.Scolaris_CM.controllers;

import com.example.Scolaris_CM.dtos.SanctionRequest;
import com.example.Scolaris_CM.dtos.SanctionResponse;
import com.example.Scolaris_CM.models.User;
import com.example.Scolaris_CM.services.SanctionService;
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
@RequestMapping("/api/sanctions")
public class SanctionController {

    private final SanctionService sanctionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<SanctionResponse> create(
            @Validated @RequestBody SanctionRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sanctionService.create(request, currentUser));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SanctionResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(sanctionService.getByStudent(studentId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<SanctionResponse> update(@PathVariable Long id, @Validated @RequestBody SanctionRequest request) {
        return ResponseEntity.ok(sanctionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sanctionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}