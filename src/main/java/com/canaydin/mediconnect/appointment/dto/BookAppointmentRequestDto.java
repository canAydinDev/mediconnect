package com.canaydin.mediconnect.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record BookAppointmentRequestDto(

        @NotNull(message = "Doctor id cannot be null")
        @Positive(message = "Doctor id must be greater than 0")
        Long doctorId,

        @NotNull(message = "Appointment time cannot be null")
        @Future(message = "Appointment time must be in the future")
        Instant appointmentAt,

        @Size(
                max = 1000,
                message = "Reason must be at most 1000 characters"
        )
        String reason

) {
}