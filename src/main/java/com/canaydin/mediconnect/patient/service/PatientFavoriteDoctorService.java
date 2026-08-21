package com.canaydin.mediconnect.patient.service;

import com.canaydin.mediconnect.doctor.dto.DoctorDto;

import java.util.List;

public interface PatientFavoriteDoctorService {

    DoctorDto saveFavoriteDoctor(
            Long doctorId,
            String patientEmail
    );

    void unsaveFavoriteDoctor(
            Long doctorId,
            String patientEmail
    );

    List<DoctorDto> getMyFavoriteDoctors(
            String patientEmail
    );
}