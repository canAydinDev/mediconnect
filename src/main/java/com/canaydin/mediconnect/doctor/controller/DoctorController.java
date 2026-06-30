package com.canaydin.mediconnect.doctor.controller;

import com.canaydin.mediconnect.doctor.dto.DoctorDto;
import com.canaydin.mediconnect.doctor.dto.DoctorRequestDto;
import com.canaydin.mediconnect.doctor.service.DoctorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
@Validated
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping(version = "1.0")
    public ResponseEntity<DoctorDto> createDoctor(
            @Valid @RequestBody DoctorRequestDto doctorRequestDto
    ) {
        DoctorDto doctorDto = doctorService.createDoctor(doctorRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorDto);
    }

    @GetMapping(version = "1.0")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping(value = "/active", version = "1.0")
    public ResponseEntity<List<DoctorDto>> getActiveDoctors() {
        return ResponseEntity.ok(doctorService.getActiveDoctors());
    }

    @GetMapping(value = "/by-clinic/{clinicId}", version = "1.0")
    public ResponseEntity<List<DoctorDto>> getDoctorsByClinicId(
            @PathVariable Long clinicId
    ) {
        return ResponseEntity.ok(doctorService.getDoctorsByClinicId(clinicId));
    }

    @GetMapping(value = "/by-specialty", version = "1.0")
    public ResponseEntity<List<DoctorDto>> getDoctorsBySpecialty(
            @RequestParam
            @NotBlank(message = "Specialty cannot be blank")
            String specialty
    ) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialty(specialty));
    }

    @GetMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        DoctorDto doctorDto = doctorService.getDoctorById(id);

        return ResponseEntity.ok(doctorDto);
    }

    @PutMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<DoctorDto> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequestDto doctorRequestDto
    ) {
        DoctorDto doctorDto = doctorService.updateDoctor(id, doctorRequestDto);

        return ResponseEntity.ok(doctorDto);
    }

    @DeleteMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/by-clinic/{clinicId}/active", version = "1.0")
    public ResponseEntity<List<DoctorDto>> getActiveDoctorsByClinicId(
            @PathVariable Long clinicId
    ) {
        return ResponseEntity.ok(doctorService.getActiveDoctorsByClinicId(clinicId));
    }
}