package com.canaydin.mediconnect.doctor.service;

import com.canaydin.mediconnect.doctor.dto.ClinicAdminDoctorRequestDto;
import com.canaydin.mediconnect.doctor.dto.DoctorDto;
import com.canaydin.mediconnect.doctor.dto.DoctorRequestDto;

import java.util.List;

public interface DoctorService {

    DoctorDto createDoctor(DoctorRequestDto doctorRequestDto);

    DoctorDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto);

    DoctorDto updateDoctorActiveStatus(
            Long id,
            Boolean active
    );

    void deleteDoctor(Long id);

    void deleteMyClinicDoctor(
            Long doctorId,
            String clinicAdminEmail
    );

    DoctorDto getDoctorById(Long id);

    List<DoctorDto> getAllDoctors();

    List<DoctorDto> getDoctorsByClinicId(Long clinicId);

    List<DoctorDto> getDoctorsBySpecialty(String specialty);

    List<DoctorDto> getActiveDoctors();

    List<DoctorDto> getActiveDoctorsByClinicId(Long clinicId);

    List<DoctorDto> getMyClinicDoctors(String clinicAdminEmail);

    DoctorDto updateMyClinicDoctorActiveStatus(
            Long doctorId,
            Boolean active,
            String clinicAdminEmail
    );

    DoctorDto createDoctorForMyClinic(
            ClinicAdminDoctorRequestDto doctorRequestDto,
            String clinicAdminEmail
    );
}