package com.example.Scolaris_CM.services;

import com.example.Scolaris_CM.dtos.LoginRequest;
import com.example.Scolaris_CM.dtos.LoginResponse;
import com.example.Scolaris_CM.models.RefreshToken;
import com.example.Scolaris_CM.models.User;
import com.example.Scolaris_CM.repository.RefreshTokenRepo;
import com.example.Scolaris_CM.repository.UserRepo;
import com.example.Scolaris_CM.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found!!"));

        refreshTokenRepo.revokeAllByUser(user);

        String accessToken = jwtService.generateToken(user);
        String refreshTokenStr = jwtService.generateRefreshToken(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenStr);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);
        refreshTokenRepo.save(refreshToken);

        return new LoginResponse(accessToken, refreshTokenStr, user.getRole(), user.getId());
    }

    @Transactional
    public void logout(String refreshTokenStr) {
        refreshTokenRepo.findByToken(refreshTokenStr).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepo.save(token);
        });
    }
}