package com.canaydin.mediconnect.patient.service;

import com.canaydin.mediconnect.patient.dto.PatientProfileDto;
import com.canaydin.mediconnect.patient.dto.PatientProfileImageDto;
import com.canaydin.mediconnect.patient.dto.PatientProfileRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface PatientProfileService {

    PatientProfileDto createOrUpdateMyProfile(
            PatientProfileRequestDto request,
            String patientEmail
    );

    PatientProfileDto uploadMyProfileImage(
            MultipartFile image,
            String patientEmail
    );

    PatientProfileDto getMyProfile(
            String patientEmail
    );

    PatientProfileImageDto getMyProfileImage(
            String patientEmail
    );
}