package com.example.Scolaris_CM.controllers;

import com.example.Scolaris_CM.dtos.SequenceRequest;
import com.example.Scolaris_CM.dtos.SequenceResponse;
import com.example.Scolaris_CM.services.SequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sequences")
@PreAuthorize("hasRole('ADMIN')")
public class SequenceController {

    private final SequenceService sequenceService;

    @PostMapping
    public ResponseEntity<SequenceResponse> create(@Validated @RequestBody SequenceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sequenceService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<SequenceResponse>> getAll(
            @RequestParam(required = false) Long schoolYearId
    ) {
        if (schoolYearId != null) {
            return ResponseEntity.ok(sequenceService.getBySchoolYear(schoolYearId));
        }
        return ResponseEntity.ok(sequenceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SequenceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sequenceService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SequenceResponse> update(@PathVariable Long id, @Validated @RequestBody SequenceRequest request) {
        return ResponseEntity.ok(sequenceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sequenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}