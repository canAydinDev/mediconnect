package com.canaydin.mediconnect.clinic.dto;

import java.math.BigDecimal;

public record ClinicDto(
        Long id,
        String name,
        String logoUrl,
        String category,
        String city,
        BigDecimal rating,
        String description
) {
}