package com.canaydin.mediconnect.clinic.service;

import com.canaydin.mediconnect.clinic.dto.ClinicAdminDto;
import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.dto.ClinicRequestDto;
import org.springframework.data.domain.Page;


public interface ClinicService {

    Page<ClinicDto> getAllClinics(int page, int size, String sortBy, String direction);

    Page<ClinicAdminDto> getAllClinicsForAdmin(
            int page,
            int size,
            String sortBy,
            String direction
    );

    ClinicDto getClinicById(Long id);

    ClinicAdminDto getClinicByIdForAdmin(Long id);


    ClinicAdminDto saveClinic(ClinicRequestDto clinicRequestDto);

    ClinicAdminDto updateClinicById(Long id, ClinicRequestDto clinicRequestDto);

    ClinicAdminDto updateClinicStatus(
            Long id,
            String status
    );

}
