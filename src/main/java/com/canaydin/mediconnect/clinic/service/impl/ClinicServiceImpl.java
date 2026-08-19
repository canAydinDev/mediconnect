package com.canaydin.mediconnect.clinic.service.impl;

import com.canaydin.mediconnect.clinic.dto.ClinicAdminDto;
import com.canaydin.mediconnect.clinic.dto.ClinicDto;
import com.canaydin.mediconnect.clinic.dto.ClinicRequestDto;
import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.clinic.enums.ClinicStatus;
import com.canaydin.mediconnect.clinic.repository.ClinicRepository;
import com.canaydin.mediconnect.clinic.service.ClinicService;
import com.canaydin.mediconnect.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;


    @Override
    @Cacheable(
            cacheNames = "clinicList"
    )
    @Transactional(readOnly = true)
    public Page<ClinicDto> getAllClinics(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = buildSort(sortBy, direction);

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        return clinicRepository.findByStatus(
                        ClinicStatus.ACTIVE,
                        pageable
                )
                .map(this::mapToClinicDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClinicAdminDto> getAllClinicsForAdmin(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = buildSort(sortBy, direction);

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        return clinicRepository.findAll(pageable)
                .map(this::mapToAdminClinicDto);
    }


    @Override
    @Cacheable(cacheNames = "clinicById",
            key = "#id")
    @Transactional(readOnly = true)
    public ClinicDto getClinicById(Long id) {

        Clinic clinic = clinicRepository
                .findByIdAndStatus(
                        id,
                        ClinicStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Clinic",
                                "id",
                                id
                        )
                );

        return mapToClinicDto(clinic);
    }

    @Override
    @Transactional(readOnly = true)
    public ClinicAdminDto getClinicByIdForAdmin(Long id) {

        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Clinic",
                                "id",
                                id
                        )
                );

        return mapToAdminClinicDto(clinic);
    }


    @Override
    @CacheEvict(
            cacheNames = "clinicList",
            allEntries = true
    )
    @Transactional
    public ClinicAdminDto saveClinic(
            ClinicRequestDto clinicRequestDto
    ) {

        Clinic clinic = mapToClinic(clinicRequestDto);

        Clinic savedClinic =
                clinicRepository.save(clinic);

        return mapToAdminClinicDto(savedClinic);
    }


    @Override
    @Caching(evict = {
            @CacheEvict(
                    cacheNames = "clinicById",
                    key = "#id"
            ),
            @CacheEvict(
                    cacheNames = "clinicList",
                    allEntries = true
            )
    })
    @Transactional
    public ClinicAdminDto updateClinicById(
            Long id,
            ClinicRequestDto clinicRequestDto
    ) {

        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Clinic",
                                "id",
                                id
                        )
                );

        clinic.setName(clinicRequestDto.name());
        clinic.setLogoUrl(clinicRequestDto.logoUrl());
        clinic.setCategory(clinicRequestDto.category());
        clinic.setCity(clinicRequestDto.city());
        clinic.setAddress(clinicRequestDto.address());
        clinic.setPhone(clinicRequestDto.phone());
        clinic.setEmail(clinicRequestDto.email());
        clinic.setRating(clinicRequestDto.rating());
        clinic.setDescription(clinicRequestDto.description());

        return mapToAdminClinicDto(clinic);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(
                    cacheNames = "clinicById",
                    key = "#id"
            ),
            @CacheEvict(
                    cacheNames = "clinicList",
                    allEntries = true
            )
    })
    @Transactional
    public ClinicAdminDto updateClinicStatus(
            Long id,
            String status
    ) {

        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Clinic",
                                "id",
                                id
                        )
                );

        ClinicStatus clinicStatus =
                ClinicStatus.valueOf(
                        status.trim().toUpperCase()
                );

        clinic.setStatus(clinicStatus);

        return mapToAdminClinicDto(clinic);
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

    private ClinicAdminDto mapToAdminClinicDto(Clinic clinic) {

        return new ClinicAdminDto(
                clinic.getId(),
                clinic.getName(),
                clinic.getLogoUrl(),
                clinic.getCategory(),
                clinic.getCity(),
                clinic.getAddress(),
                clinic.getPhone(),
                clinic.getEmail(),
                clinic.getRating(),
                clinic.getDescription(),
                clinic.getStatus()
        );
    }


    private Clinic mapToClinic(
            ClinicRequestDto clinicRequestDto
    ) {

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
        clinic.setStatus(ClinicStatus.ACTIVE);

        return clinic;
    }

    private Sort buildSort(
            String sortBy,
            String direction
    ) {

        List<String> allowedSortFields = List.of(
                "id",
                "name",
                "category",
                "city",
                "rating"
        );

        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "name";
        }

        return direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
    }
}