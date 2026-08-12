package com.canaydin.mediconnect.clinic.dto;

import com.canaydin.mediconnect.clinic.enums.ClinicStatus;

import java.math.BigDecimal;

public record ClinicAdminDto(
        Long id,
        String name,
        String logoUrl,
        String category,
        String city,
        String address,
        String phone,
        String email,
        BigDecimal rating,
        String description,
        ClinicStatus status
) {
}