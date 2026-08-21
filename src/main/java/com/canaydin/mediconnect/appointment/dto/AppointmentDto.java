package com.canaydin.mediconnect.appointment.dto;

import com.canaydin.mediconnect.appointment.enums.AppointmentStatus;

import java.time.Instant;

public record AppointmentDto(

        Long id,

        Long doctorId,
        String doctorName,
        String specialty,

        Long clinicId,
        String clinicName,

        Instant appointmentAt,
        AppointmentStatus status,
        String reason,

        Instant createdAt,
        Instant updatedAt

) {
}