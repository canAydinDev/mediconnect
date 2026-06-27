package com.canaydin.mediconnect.clinic.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ClinicRequestDto(

        @NotBlank(message = "Clinic name cannot be empty")
        @Size(min = 2, max = 100, message = "Clinic name must be between 2 and 100 characters")
        String name,

        @Size(max = 255, message = "Logo URL must be at most 255 characters")
        String logoUrl,

        @NotBlank(message = "Category cannot be empty")
        @Size(max = 100, message = "Category must be at most 100 characters")
        String category,

        @NotBlank(message = "City cannot be empty")
        @Size(max = 100, message = "City must be at most 100 characters")
        String city,

        @Size(max = 255, message = "Address must be at most 255 characters")
        String address,

        @Size(max = 50, message = "Phone must be at most 50 characters")
        String phone,

        @Email(message = "Invalid email address")
        @Size(max = 100, message = "Email must be at most 100 characters")
        String email,

        @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
        @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
        BigDecimal rating,

        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description
) {
}