package com.canaydin.mediconnect.clinic.controller;

import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.dto.ClinicRequestDto;
import com.canaydin.mediconnect.clinic.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clinics")
public class ClinicController {

    private final ClinicService clinicService;

    @GetMapping(version = "1.0")
    public ResponseEntity<List<ClinicDto>> getAllClinics() {
        return ResponseEntity.ok(clinicService.getAllClinics());
    }

    @GetMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<ClinicDto> getClinicById(@PathVariable Long id) {
        return ResponseEntity.ok(clinicService.getClinicById(id));
    }

    @PostMapping(version = "1.0")
    public ResponseEntity<ClinicDto> saveClinic(@Valid @RequestBody ClinicRequestDto clinicRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(clinicService.saveClinic(clinicRequestDto));
    }

    @PutMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<ClinicDto> updateClinicById(
            @PathVariable Long id,
            @Valid @RequestBody ClinicRequestDto clinicRequestDto
    ) {
        return ResponseEntity.ok(clinicService.updateClinicById(id, clinicRequestDto));
    }

    @DeleteMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<Void> deleteClinicById(@PathVariable Long id) {
        clinicService.deleteClinicById(id);
        return ResponseEntity.noContent().build();
    }
    //hello
}