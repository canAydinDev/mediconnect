package com.canaydin.mediconnect.doctor.controller;

import com.canaydin.mediconnect.doctor.dto.ClinicAdminDoctorRequestDto;
import com.canaydin.mediconnect.doctor.dto.DoctorActiveStatusRequest;
import com.canaydin.mediconnect.doctor.dto.DoctorDto;
import com.canaydin.mediconnect.doctor.dto.DoctorRequestDto;
import com.canaydin.mediconnect.doctor.service.DoctorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/doctors")
@Validated
public class DoctorController {

    private final DoctorService doctorService;


    // =========================
    // ADMIN OPERATIONS
    // =========================

    @PostMapping(version = "1.0")
    public ResponseEntity<DoctorDto> createDoctor(
            @Valid @RequestBody DoctorRequestDto doctorRequestDto
    ) {

        DoctorDto doctorDto =
                doctorService.createDoctor(doctorRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorDto);
    }


    @PutMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<DoctorDto> updateDoctor(

            @PathVariable
            @Positive(message = "Doctor id must be greater than 0")
            Long id,

            @Valid
            @RequestBody
            DoctorRequestDto doctorRequestDto
    ) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(
                        id,
                        doctorRequestDto
                )
        );
    }

    @PatchMapping(
            value = "/{id}/active",
            version = "1.0"
    )
    public ResponseEntity<DoctorDto> updateDoctorActiveStatus(

            @PathVariable
            @Positive(message = "Doctor id must be greater than 0")
            Long id,

            @Valid
            @RequestBody
            DoctorActiveStatusRequest request
    ) {

        return ResponseEntity.ok(
                doctorService.updateDoctorActiveStatus(
                        id,
                        request.active()
                )
        );
    }


    @DeleteMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<Void> deleteDoctor(

            @PathVariable
            @Positive(message = "Doctor id must be greater than 0")
            Long id
    ) {

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }


    // =========================
    // CLINIC ADMIN OPERATIONS
    // =========================

    @PostMapping(
            value = "/clinic-admin",
            version = "1.0"
    )
    public ResponseEntity<DoctorDto> createDoctorForMyClinic(

            @Valid
            @RequestBody
            ClinicAdminDoctorRequestDto doctorRequestDto,

            Authentication authentication
    ) {

        DoctorDto doctorDto =
                doctorService.createDoctorForMyClinic(
                        doctorRequestDto,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorDto);
    }


    @GetMapping(
            value = "/clinic-admin",
            version = "1.0"
    )
    public ResponseEntity<List<DoctorDto>> getMyClinicDoctors(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                doctorService.getMyClinicDoctors(
                        authentication.getName()
                )
        );
    }


    @PatchMapping(
            value = "/clinic-admin/{doctorId}/active",
            version = "1.0"
    )
    public ResponseEntity<DoctorDto> updateMyClinicDoctorActiveStatus(

            @PathVariable
            @Positive(message = "Doctor id must be greater than 0")
            Long doctorId,

            @Valid
            @RequestBody
            DoctorActiveStatusRequest request,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                doctorService.updateMyClinicDoctorActiveStatus(
                        doctorId,
                        request.active(),
                        authentication.getName()
                )
        );
    }

    @DeleteMapping(
            value = "/clinic-admin/{doctorId}",
            version = "1.0"
    )
    public ResponseEntity<Void> deleteMyClinicDoctor(

            @PathVariable
            @Positive(message = "Doctor id must be greater than 0")
            Long doctorId,

            Authentication authentication
    ) {

        doctorService.deleteMyClinicDoctor(
                doctorId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }


    // =========================
    // READ OPERATIONS
    // =========================

    @GetMapping(version = "1.0")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }


    @GetMapping(value = "/active", version = "1.0")
    public ResponseEntity<List<DoctorDto>> getActiveDoctors() {

        return ResponseEntity.ok(
                doctorService.getActiveDoctors()
        );
    }


    @GetMapping(
            value = "/by-clinic/{clinicId}",
            version = "1.0"
    )
    public ResponseEntity<List<DoctorDto>> getDoctorsByClinicId(

            @PathVariable
            @Positive(message = "Clinic id must be greater than 0")
            Long clinicId
    ) {

        return ResponseEntity.ok(
                doctorService.getDoctorsByClinicId(clinicId)
        );
    }


    @GetMapping(
            value = "/by-clinic/{clinicId}/active",
            version = "1.0"
    )
    public ResponseEntity<List<DoctorDto>> getActiveDoctorsByClinicId(

            @PathVariable
            @Positive(message = "Clinic id must be greater than 0")
            Long clinicId
    ) {

        return ResponseEntity.ok(
                doctorService.getActiveDoctorsByClinicId(clinicId)
        );
    }


    @GetMapping(
            value = "/by-specialty",
            version = "1.0"
    )
    public ResponseEntity<List<DoctorDto>> getDoctorsBySpecialty(

            @RequestParam
            @NotBlank(message = "Specialty cannot be blank")
            String specialty
    ) {

        return ResponseEntity.ok(
                doctorService.getDoctorsBySpecialty(specialty)
        );
    }


    @GetMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<DoctorDto> getDoctorById(

            @PathVariable
            @Positive(message = "Doctor id must be greater than 0")
            Long id
    ) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(id)
        );
    }
}