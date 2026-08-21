package com.canaydin.mediconnect.patient.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientProfileRequestDto(

        @Size(
                max = 50,
                message = "Phone must be at most 50 characters"
        )
        String phone,

        @PastOrPresent(
                message = "Date of birth cannot be in the future"
        )
        LocalDate dateOfBirth,

        @Size(
                max = 100,
                message = "City must be at most 100 characters"
        )
        String city,

        @Size(
                max = 500,
                message = "Address must be at most 500 characters"
        )
        String address

) {
}