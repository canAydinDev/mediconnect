package com.canaydin.mediconnect.appointment.dto;

import com.canaydin.mediconnect.appointment.enums.AppointmentStatus;

import java.time.Instant;

public record ClinicAdminAppointmentDto(

        Long id,

        Long patientId,
        String patientFullName,
        String patientEmail,

        Long doctorId,
        String doctorName,
        String specialty,

        Long clinicId,
        String clinicName,

        Instant appointmentAt,
        AppointmentStatus status,

        String reason,
        String internalNotes,

        Instant createdAt,
        Instant updatedAt

) {
}