package com.canaydin.mediconnect.patient.controller;

import com.canaydin.mediconnect.doctor.dto.DoctorDto;
import com.canaydin.mediconnect.patient.service.PatientFavoriteDoctorService;
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
@RequestMapping("/patients/me/favorite-doctors")
@Validated
public class PatientFavoriteDoctorController {

    private final PatientFavoriteDoctorService
            patientFavoriteDoctorService;


    @PostMapping(
            value = "/{doctorId}",
            version = "1.0"
    )
    public ResponseEntity<DoctorDto> saveFavoriteDoctor(

            @PathVariable
            @Positive(
                    message =
                            "Doctor id must be greater than 0"
            )
            Long doctorId,

            Authentication authentication
    ) {

        DoctorDto doctorDto =
                patientFavoriteDoctorService
                        .saveFavoriteDoctor(
                                doctorId,
                                authentication.getName()
                        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorDto);
    }


    @DeleteMapping(
            value = "/{doctorId}",
            version = "1.0"
    )
    public ResponseEntity<Void> unsaveFavoriteDoctor(

            @PathVariable
            @Positive(
                    message =
                            "Doctor id must be greater than 0"
            )
            Long doctorId,

            Authentication authentication
    ) {

        patientFavoriteDoctorService
                .unsaveFavoriteDoctor(
                        doctorId,
                        authentication.getName()
                );

        return ResponseEntity
                .noContent()
                .build();
    }


    @GetMapping(version = "1.0")
    public ResponseEntity<List<DoctorDto>>
    getMyFavoriteDoctors(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                patientFavoriteDoctorService
                        .getMyFavoriteDoctors(
                                authentication.getName()
                        )
        );
    }
}