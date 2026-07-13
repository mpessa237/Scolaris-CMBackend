package com.example.Scolaris_CM.dtos;

import com.example.Scolaris_CM.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Role role;
    private Long id;
}
