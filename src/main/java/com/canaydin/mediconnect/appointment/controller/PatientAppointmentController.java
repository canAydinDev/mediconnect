package com.canaydin.mediconnect.appointment.controller;

import com.canaydin.mediconnect.appointment.dto.AppointmentDto;
import com.canaydin.mediconnect.appointment.dto.BookAppointmentRequestDto;
import com.canaydin.mediconnect.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients/me/appointments")
@Validated
public class PatientAppointmentController {

    private final AppointmentService appointmentService;


    @PostMapping(version = "1.0")
    public ResponseEntity<AppointmentDto> bookAppointment(

            @Valid
            @RequestBody
            BookAppointmentRequestDto request,

            Authentication authentication
    ) {

        AppointmentDto appointmentDto =
                appointmentService.bookMyAppointment(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentDto);
    }


    @PatchMapping(
            value = "/{appointmentId}/cancel",
            version = "1.0"
    )
    public ResponseEntity<AppointmentDto> cancelAppointment(

            @PathVariable
            @Positive(
                    message =
                            "Appointment id must be greater than 0"
            )
            Long appointmentId,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                appointmentService
                        .cancelMyAppointment(
                                appointmentId,
                                authentication.getName()
                        )
        );
    }


    @GetMapping(version = "1.0")
    public ResponseEntity<List<AppointmentDto>>
    getMyAppointments(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                appointmentService
                        .getMyAppointments(
                                authentication.getName()
                        )
        );
    }
}