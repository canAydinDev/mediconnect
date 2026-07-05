package com.canaydin.mediconnect.security.auth.controller;

import com.canaydin.mediconnect.security.auth.dto.AuthResponse;
import com.canaydin.mediconnect.security.auth.dto.LoginRequest;
import com.canaydin.mediconnect.security.auth.dto.RegisterRequest;
import com.canaydin.mediconnect.security.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register", version = "1.0")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping(value = "/login", version = "1.0")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}