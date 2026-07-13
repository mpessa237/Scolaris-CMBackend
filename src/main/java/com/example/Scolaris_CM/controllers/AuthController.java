package com.example.Scolaris_CM.controllers;

import com.example.Scolaris_CM.dtos.LoginRequest;
import com.example.Scolaris_CM.dtos.LoginResponse;
import com.example.Scolaris_CM.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest){

        return ResponseEntity.ok(authService.login(loginRequest));
    }

}
