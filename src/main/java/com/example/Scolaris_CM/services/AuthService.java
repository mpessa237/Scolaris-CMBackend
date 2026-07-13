package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.LoginRequest;
import com.example.Scolaris_CM.dtos.LoginResponse;
import com.example.Scolaris_CM.models.User;
import com.example.Scolaris_CM.repository.UserRepo;
import com.example.Scolaris_CM.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final JwtService jwtService;


    public @Nullable LoginResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("user not found!!"));


        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken, user.getRole(), user.getId());

    }
}
