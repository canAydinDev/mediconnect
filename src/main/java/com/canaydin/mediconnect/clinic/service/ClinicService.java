package com.canaydin.mediconnect.clinic.service;

import com.canaydin.mediconnect.clinic.dto.ClinicAdminDto;
import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.dto.ClinicRequestDto;

import java.util.List;

public interface ClinicService {

    List<ClinicDto> getAllClinics();

    ClinicDto getClinicById(Long id);


    ClinicDto saveClinic(ClinicRequestDto clinicRequestDto);

    ClinicAdminDto updateClinicById(Long id, ClinicRequestDto clinicRequestDto);


    void deleteClinicById(Long id);
}
