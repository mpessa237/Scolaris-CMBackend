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
public class SequenceController {

    private final SequenceService sequenceService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SequenceResponse> create(@Validated @RequestBody SequenceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sequenceService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<SequenceResponse>> getAll() {
        return ResponseEntity.ok(sequenceService.getAll());
    }
}