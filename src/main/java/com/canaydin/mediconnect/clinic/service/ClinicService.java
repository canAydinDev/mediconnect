package com.canaydin.mediconnect.clinic.service;

import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.dto.ClinicRequestDto;
import com.canaydin.mediconnect.clinic.entity.Clinic;

import java.util.List;

public interface ClinicService {

    List<ClinicDto> getAllClinics();

    ClinicDto getClinicById(Long id);

    ClinicDto saveClinic(ClinicRequestDto clinicRequestDto);

    ClinicDto updateClinicById(Long id, ClinicRequestDto clinicRequestDto);

    void deleteClinicById(Long id);
}
