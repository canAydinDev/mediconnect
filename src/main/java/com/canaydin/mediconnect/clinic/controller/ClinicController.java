package com.canaydin.mediconnect.clinic.controller;

import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}