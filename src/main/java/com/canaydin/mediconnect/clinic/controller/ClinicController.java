package com.canaydin.mediconnect.clinic.controller;

import com.canaydin.mediconnect.clinic.dto.ClinicAdminDto;
import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.dto.ClinicRequestDto;
import com.canaydin.mediconnect.clinic.service.ClinicService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/clinics")
@Validated
public class ClinicController {

    private final ClinicService clinicService;

    @GetMapping(version = "1.0")
    public ResponseEntity<Page<ClinicDto>> getAllClinics(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size must be at most 100")
            int size,

            @RequestParam(defaultValue = "name")
            String sortBy,
            @RequestParam(defaultValue = "asc")
            @Pattern(
                    regexp = "(?i)asc|desc",
                    message = "Direction must be asc or desc"
            )
            String direction
    ) {
        return ResponseEntity.ok(clinicService.getAllClinics(page, size, sortBy, direction));
    }

    @GetMapping(value = "/admin", version = "1.0")
    public ResponseEntity<Page<ClinicAdminDto>> getAllClinicsForAdmin(

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size must be at most 100")
            int size,

            @RequestParam(defaultValue = "name")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            @Pattern(
                    regexp = "(?i)asc|desc",
                    message = "Direction must be asc or desc"
            )
            String direction
    ) {

        return ResponseEntity.ok(
                clinicService.getAllClinicsForAdmin(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }


    @GetMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<ClinicDto> getClinicById(@PathVariable
                                                   @Positive(message = "Clinic id must be greater than 0")
                                                   Long id) {
        return ResponseEntity.ok(clinicService.getClinicById(id));
    }


    @GetMapping(value = "/admin/{id}", version = "1.0")
    public ResponseEntity<ClinicAdminDto> getClinicByIdForAdmin(@PathVariable
                                                                @Positive(message = "Clinic id must be greater than 0")
                                                                Long id) {
        return ResponseEntity.ok(clinicService.getClinicByIdForAdmin(id));
    }


    @PostMapping(value = "/admin", version = "1.0")
    public ResponseEntity<ClinicAdminDto> saveClinic(@Valid @RequestBody ClinicRequestDto clinicRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(clinicService.saveClinic(clinicRequestDto));
    }

    @PutMapping(value = "/admin/{id}", version = "1.0")
    public ResponseEntity<ClinicAdminDto> updateClinicById(
            @PathVariable @Positive(message = "Clinic id must be greater than 0")
            Long id,
            @Valid @RequestBody ClinicRequestDto clinicRequestDto
    ) {
        return ResponseEntity.ok(
                clinicService.updateClinicById(
                        id,
                        clinicRequestDto
                )
        );
    }

    @PatchMapping(
            value = "/admin/{id}/status",
            version = "1.0"
    )
    public ResponseEntity<ClinicAdminDto> updateClinicStatus(

            @PathVariable
            @Positive(message = "Clinic id must be greater than 0")
            Long id,

            @RequestParam
            @Pattern(
                    regexp = "(?i)ACTIVE|INACTIVE",
                    message = "Status must be ACTIVE or INACTIVE"
            )
            String status
    ) {

        return ResponseEntity.ok(
                clinicService.updateClinicStatus(
                        id,
                        status
                )
        );
    }
}