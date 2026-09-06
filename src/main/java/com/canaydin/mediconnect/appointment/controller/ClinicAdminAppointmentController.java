package com.canaydin.mediconnect.appointment.controller;

import com.canaydin.mediconnect.appointment.dto.ClinicAdminAppointmentDto;
import com.canaydin.mediconnect.appointment.dto.ClinicAdminAppointmentUpdateRequestDto;
import com.canaydin.mediconnect.appointment.service.ClinicAdminAppointmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments/clinic-admin")
@Validated
public class ClinicAdminAppointmentController {

    private final ClinicAdminAppointmentService
            clinicAdminAppointmentService;


    @GetMapping(
            value = "/doctors/{doctorId}",
            version = "1.0"
    )
    public ResponseEntity<List<ClinicAdminAppointmentDto>>
    getDoctorAppointments(

            @PathVariable
            @Positive(
                    message =
                            "Doctor id must be greater than 0"
            )
            Long doctorId,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                clinicAdminAppointmentService
                        .getDoctorAppointments(
                                doctorId,
                                authentication.getName()
                        )
        );
    }


    @PatchMapping(
            value = "/{appointmentId}",
            version = "1.0"
    )
    public ResponseEntity<ClinicAdminAppointmentDto>
    updateAppointment(

            @PathVariable
            @Positive(
                    message =
                            "Appointment id must be greater than 0"
            )
            Long appointmentId,

            @Valid
            @RequestBody
            ClinicAdminAppointmentUpdateRequestDto request,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                clinicAdminAppointmentService
                        .updateAppointment(
                                appointmentId,
                                request,
                                authentication.getName()
                        )
        );
    }
}