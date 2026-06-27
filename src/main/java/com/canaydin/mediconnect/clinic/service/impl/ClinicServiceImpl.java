package com.canaydin.mediconnect.clinic.service.impl;

import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.dto.ClinicRequestDto;
import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.clinic.repository.ClinicRepository;
import com.canaydin.mediconnect.clinic.service.ClinicService;
import com.canaydin.mediconnect.exception.ResourceNotFoundException;
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

    @Override
    public ClinicDto saveClinic(ClinicRequestDto clinicRequestDto) {
        Clinic clinic = mapToClinic(clinicRequestDto);
        clinicRepository.save(clinic);
        return mapToClinicDto(clinic);


    }

    @Override
    public ClinicDto updateClinicById(Long id, ClinicRequestDto clinicRequestDto) {
        Clinic clinic = clinicRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Clinic", "id", id));
        clinic.setName(clinicRequestDto.name());
        clinic.setLogoUrl(clinicRequestDto.logoUrl());
        clinic.setCategory(clinicRequestDto.category());
        clinic.setCity(clinicRequestDto.city());
        clinic.setAddress(clinicRequestDto.address());
        clinic.setPhone(clinicRequestDto.phone());
        clinic.setEmail(clinicRequestDto.email());
        clinic.setRating(clinicRequestDto.rating());
        clinic.setDescription(clinicRequestDto.description());

        Clinic updatedClinic = clinicRepository.save(clinic);

        return mapToClinicDto(updatedClinic);

    }

    @Override
    public void deleteClinicById(Long id) {
        Clinic clinic = clinicRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Clinic", "id", id));
        clinicRepository.delete(clinic);

    }

    @Override
    public ClinicDto getClinicById(Long id) {
       Clinic clinic = clinicRepository.findById(id).
               orElseThrow(() -> new ResourceNotFoundException("Clinic", "id", id));
       return mapToClinicDto(clinic);

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

    private Clinic mapToClinic(ClinicRequestDto clinicRequestDto) {
        Clinic clinic = new Clinic();

        clinic.setName(clinicRequestDto.name());
        clinic.setLogoUrl(clinicRequestDto.logoUrl());
        clinic.setCategory(clinicRequestDto.category());
        clinic.setCity(clinicRequestDto.city());
        clinic.setAddress(clinicRequestDto.address());
        clinic.setPhone(clinicRequestDto.phone());
        clinic.setEmail(clinicRequestDto.email());
        clinic.setRating(clinicRequestDto.rating());
        clinic.setDescription(clinicRequestDto.description());
        return clinic;
    }
}