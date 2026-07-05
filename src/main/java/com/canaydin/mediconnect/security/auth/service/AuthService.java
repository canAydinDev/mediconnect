package com.canaydin.mediconnect.security.auth.service;

import com.canaydin.mediconnect.security.auth.dto.AuthResponse;
import com.canaydin.mediconnect.security.auth.dto.LoginRequest;
import com.canaydin.mediconnect.security.auth.dto.RegisterRequest;
import com.canaydin.mediconnect.security.user.entity.UserAccount;
import com.canaydin.mediconnect.security.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
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
                .role(com.canaydin.mediconnect.security.user.enums.Role.PATIENT)
                .active(true)
                .build();

        UserAccount savedUser = userAccountRepository.save(user);

        return new AuthResponse(
                "User registered successfully",
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                null
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (DisabledException exception) {
            throw new DisabledException("User account is inactive");
        }

        UserAccount user = userAccountRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        return new AuthResponse(
                "Login successful",
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                null
        );
    }
}