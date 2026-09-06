package com.canaydin.mediconnect.appointment.dto;

import com.canaydin.mediconnect.appointment.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClinicAdminAppointmentUpdateRequestDto(

        @NotNull(message = "Appointment status cannot be null")
        AppointmentStatus status,

        @Size(
                max = 2000,
                message = "Internal notes must be at most 2000 characters"
        )
        String internalNotes

) {
}