package com.canaydin.mediconnect.clinic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clinics")
public class ClinicController {

    @GetMapping(version="1.0")
    public ResponseEntity<String> getAllClinics(){
        return ResponseEntity.ok("Clinics");
    }
}
