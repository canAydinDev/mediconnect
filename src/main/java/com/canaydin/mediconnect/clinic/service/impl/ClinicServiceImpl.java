package com.canaydin.mediconnect.clinic.service.impl;

import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.clinic.repository.ClinicRepository;
import com.canaydin.mediconnect.clinic.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;



    @Override
    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }
}
