package com.canaydin.mediconnect.appointment.service;

import com.canaydin.mediconnect.appointment.dto.ClinicAdminAppointmentDto;
import com.canaydin.mediconnect.appointment.dto.ClinicAdminAppointmentUpdateRequestDto;

import java.util.List;

public interface ClinicAdminAppointmentService {

    List<ClinicAdminAppointmentDto> getDoctorAppointments(
            Long doctorId,
            String clinicAdminEmail
    );

    ClinicAdminAppointmentDto updateAppointment(
            Long appointmentId,
            ClinicAdminAppointmentUpdateRequestDto request,
            String clinicAdminEmail
    );
}