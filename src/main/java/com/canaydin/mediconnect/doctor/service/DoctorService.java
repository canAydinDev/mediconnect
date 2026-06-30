package com.canaydin.mediconnect.doctor.service;

import com.canaydin.mediconnect.doctor.dto.DoctorDto;
import com.canaydin.mediconnect.doctor.dto.DoctorRequestDto;

import java.util.List;

public interface DoctorService {

    DoctorDto createDoctor(DoctorRequestDto doctorRequestDto);

    DoctorDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto);

    void deleteDoctor(Long id);

    DoctorDto getDoctorById(Long id);

    List<DoctorDto> getAllDoctors();

    List<DoctorDto> getDoctorsByClinicId(Long clinicId);

    List<DoctorDto> getDoctorsBySpecialty(String specialty);

    List<DoctorDto> getActiveDoctors();

    List<DoctorDto> getActiveDoctorsByClinicId(Long clinicId);
}