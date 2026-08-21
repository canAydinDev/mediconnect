package com.canaydin.mediconnect.patient.controller;

import com.canaydin.mediconnect.patient.dto.PatientProfileDto;
import com.canaydin.mediconnect.patient.dto.PatientProfileImageDto;
import com.canaydin.mediconnect.patient.dto.PatientProfileRequestDto;
import com.canaydin.mediconnect.patient.service.PatientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients/me/profile")
public class PatientProfileController {

    private final PatientProfileService patientProfileService;


    @PutMapping(version = "1.0")
    public ResponseEntity<PatientProfileDto> createOrUpdateMyProfile(

            @Valid
            @RequestBody
            PatientProfileRequestDto request,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                patientProfileService
                        .createOrUpdateMyProfile(
                                request,
                                authentication.getName()
                        )
        );
    }


    @PutMapping(
            value = "/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            version = "1.0"
    )
    public ResponseEntity<PatientProfileDto> uploadProfileImage(

            @RequestPart("image")
            MultipartFile image,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                patientProfileService
                        .uploadMyProfileImage(
                                image,
                                authentication.getName()
                        )
        );
    }

    @GetMapping(version = "1.0")
    public ResponseEntity<PatientProfileDto> getMyProfile(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                patientProfileService.getMyProfile(
                        authentication.getName()
                )
        );
    }

    @GetMapping(
            value = "/image",
            version = "1.0"
    )
    public ResponseEntity<byte[]> getMyProfileImage(
            Authentication authentication
    ) {

        PatientProfileImageDto image =
                patientProfileService.getMyProfileImage(
                        authentication.getName()
                );

        MediaType mediaType =
                MediaType.parseMediaType(
                        image.contentType()
                );

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .contentLength(image.content().length)
                .body(image.content());
    }
}
