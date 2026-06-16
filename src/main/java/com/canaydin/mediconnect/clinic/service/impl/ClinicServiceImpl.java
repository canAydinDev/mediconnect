package com.canaydin.mediconnect.clinic.service.impl;

import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.clinic.repository.ClinicRepository;
import com.canaydin.mediconnect.clinic.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;

    @Override
    public List<ClinicDto> getAllClinics() {
        return clinicRepository.findAll()
                .stream()
                .map(this::mapToClinicDto)
                .toList();
    }

    private ClinicDto mapToClinicDto(Clinic clinic) {
        return new ClinicDto(
                clinic.getId(),
                clinic.getName(),
                clinic.getLogoUrl(),
                clinic.getCategory(),
                clinic.getCity(),
                clinic.getRating(),
                clinic.getDescription()
        );
    }
}