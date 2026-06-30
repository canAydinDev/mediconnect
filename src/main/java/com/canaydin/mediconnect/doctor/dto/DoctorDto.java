package com.canaydin.mediconnect.doctor.dto;

import java.time.Instant;

public record DoctorDto(
        Long id,
        String fullName,
        String title,
        String specialty,
        String email,
        String phone,
        String bio,
        String imageUrl,
        Integer experienceYears,
        Boolean active,
        Long clinicId,
        String clinicName,
        Instant createdAt
) {
}
