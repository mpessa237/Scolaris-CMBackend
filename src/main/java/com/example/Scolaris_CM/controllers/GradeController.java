package com.example.Scolaris_CM.controllers;

import com.example.Scolaris_CM.dtos.GradeRequest;
import com.example.Scolaris_CM.dtos.GradeResponse;
import com.example.Scolaris_CM.models.User;
import com.example.Scolaris_CM.services.GradeService;
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
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<GradeResponse> create(
            @Validated @RequestBody GradeRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gradeService.create(request, currentUser));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getByStudent(studentId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<GradeResponse> update(@PathVariable Long id, @Validated @RequestBody GradeRequest request) {
        return ResponseEntity.ok(gradeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gradeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}