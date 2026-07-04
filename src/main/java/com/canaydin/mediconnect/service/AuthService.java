package com.canaydin.mediconnect.service;

import com.canaydin.mediconnect.dto.AuthResponse;
import com.canaydin.mediconnect.dto.RegisterRequest;
import com.canaydin.mediconnect.entity.Role;
import com.canaydin.mediconnect.entity.UserAccount;
import com.canaydin.mediconnect.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userAccountRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserAccount user = UserAccount.builder()
                .fullName(request.getFullName().trim())
                .email(normalizedEmail)
                .password(encodedPassword)
                .role(Role.PATIENT)
                .active(true)
                .build();

        UserAccount savedUser = userAccountRepository.save(user);

        return new AuthResponse(
                "User registered successfully",
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }
}