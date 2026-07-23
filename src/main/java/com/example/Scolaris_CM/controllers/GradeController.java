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

    // hasAnyRole('TEACHER', 'ADMIN') : un enseignant saisit ses propres notes,
    // un admin peut intervenir en cas de correction administrative
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<GradeResponse> create(
            @Validated @RequestBody GradeRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gradeService.create(request, currentUser));
    }

    @GetMapping
    public ResponseEntity<List<GradeResponse>> getAll() {
        return ResponseEntity.ok(gradeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gradeService.getById(id));
    }

    @GetMapping("/by-student/{studentId}")
    public ResponseEntity<List<GradeResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getByStudent(studentId));
    }

    @GetMapping("/by-student/{studentId}/by-sequence/{sequenceId}")
    public ResponseEntity<List<GradeResponse>> getByStudentAndSequence(
            @PathVariable Long studentId, @PathVariable Long sequenceId
    ) {
        return ResponseEntity.ok(gradeService.getByStudentAndSequence(studentId, sequenceId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<GradeResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody GradeRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(gradeService.update(id, request, currentUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        gradeService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}