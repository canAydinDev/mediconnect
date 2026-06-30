package com.canaydin.mediconnect.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DoctorRequestDto(

        @NotBlank(message = "Doctor full name cannot be empty")
        @Size(min = 2, max = 100, message = "Doctor full name must be between 2 and 100 characters")
        String fullName,

        @Size(max = 100, message = "Title must be at most 100 characters")
        String title,

        @NotBlank(message = "Specialty cannot be empty")
        @Size(max = 100, message = "Specialty must be at most 100 characters")
        String specialty,

        @Email(message = "Invalid email address")
        @Size(max = 150, message = "Email must be at most 150 characters")
        String email,

        @Size(max = 50, message = "Phone must be at most 50 characters")
        String phone,

        @Size(max = 1000, message = "Bio must be at most 1000 characters")
        String bio,

        @Size(max = 255, message = "Image URL must be at most 255 characters")
        String imageUrl,

        @Min(value = 0, message = "Experience years cannot be negative")
        @Max(value = 70, message = "Experience years cannot be greater than 70")
        Integer experienceYears,

        Boolean active,

        @NotNull(message = "Clinic id cannot be null")
        Long clinicId
) {
}