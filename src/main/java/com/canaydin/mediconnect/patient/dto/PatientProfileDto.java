package com.canaydin.mediconnect.patient.dto;

import java.time.Instant;
import java.time.LocalDate;

public record PatientProfileDto(

        Long id,

        String fullName,
        String email,

        String phone,
        LocalDate dateOfBirth,
        String city,
        String address,

        String profileImageName,
        String profileImageType,

        Instant createdAt,
        Instant updatedAt

) {
}