package com.canaydin.mediconnect.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String message;
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String token;
}
