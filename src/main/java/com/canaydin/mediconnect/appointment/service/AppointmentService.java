package com.canaydin.mediconnect.appointment.service;

import com.canaydin.mediconnect.appointment.dto.AppointmentDto;
import com.canaydin.mediconnect.appointment.dto.BookAppointmentRequestDto;

import java.util.List;

public interface AppointmentService {

    AppointmentDto bookMyAppointment(
            BookAppointmentRequestDto request,
            String patientEmail
    );

    AppointmentDto cancelMyAppointment(
            Long appointmentId,
            String patientEmail
    );

    List<AppointmentDto> getMyAppointments(
            String patientEmail
    );
}