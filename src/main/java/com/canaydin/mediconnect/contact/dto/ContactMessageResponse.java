package com.canaydin.mediconnect.contact.dto;

import java.time.Instant;

public record ContactMessageResponse(
        Long id,
        String fullName,
        String email,
        String userType,
        String subject,
        String message,
        String status,
        Instant createdAt
) {
}