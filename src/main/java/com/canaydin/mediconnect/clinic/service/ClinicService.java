package com.canaydin.mediconnect.clinic.service;

import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.entity.Clinic;

import java.util.List;

public interface ClinicService {

    List<ClinicDto> getAllClinics();
}
