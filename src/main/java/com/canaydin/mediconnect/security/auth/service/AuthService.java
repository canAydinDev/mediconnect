package com.canaydin.mediconnect.security.auth.service;

import com.canaydin.mediconnect.exception.DuplicateResourceException;
import com.canaydin.mediconnect.exception.WeakPasswordException;
import com.canaydin.mediconnect.security.auth.dto.AuthResponse;
import com.canaydin.mediconnect.security.auth.dto.LoginRequest;
import com.canaydin.mediconnect.security.auth.dto.RegisterRequest;
import com.canaydin.mediconnect.security.jwt.JwtService;
import com.canaydin.mediconnect.security.user.entity.UserAccount;
import com.canaydin.mediconnect.security.user.enums.Role;
import com.canaydin.mediconnect.security.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (compromisedPasswordChecker.check(request.getPassword()).isCompromised()) {
            throw new WeakPasswordException("Please choose a stronger password");
        }

        if (userAccountRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException("User", "email", normalizedEmail);
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

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalizedEmail,
                        request.getPassword()
                )
        );

        UserAccount user = userAccountRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        String token = jwtService.generateToken(authentication, user);

        return new AuthResponse(
                "Login successful",
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }
}