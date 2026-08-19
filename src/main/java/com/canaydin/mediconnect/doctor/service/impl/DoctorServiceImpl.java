package com.canaydin.mediconnect.doctor.service.impl;

import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.clinic.repository.ClinicRepository;
import com.canaydin.mediconnect.doctor.dto.DoctorDto;
import com.canaydin.mediconnect.doctor.dto.DoctorRequestDto;
import com.canaydin.mediconnect.doctor.entity.Doctor;
import com.canaydin.mediconnect.doctor.repository.DoctorRepository;
import com.canaydin.mediconnect.doctor.service.DoctorService;
import com.canaydin.mediconnect.exception.ResourceNotFoundException;
import com.canaydin.mediconnect.security.user.entity.UserAccount;
import com.canaydin.mediconnect.security.user.enums.Role;
import com.canaydin.mediconnect.security.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor


public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final UserAccountRepository userAccountRepository;

    @Override
    @Transactional
    public DoctorDto createDoctor(DoctorRequestDto doctorRequestDto) {
        Clinic clinic = clinicRepository.findById(doctorRequestDto.clinicId()).orElseThrow(() -> new ResourceNotFoundException("Clinic", "id", doctorRequestDto.clinicId()));
        Doctor doctor = new Doctor();
        doctor.setFullName(doctorRequestDto.fullName());
        doctor.setEmail(doctorRequestDto.email());
        doctor.setPhone(doctorRequestDto.phone());
        doctor.setTitle(doctorRequestDto.title());
        doctor.setSpecialty(doctorRequestDto.specialty());
        doctor.setBio(doctorRequestDto.bio());
        doctor.setImageUrl(doctorRequestDto.imageUrl());
        doctor.setExperienceYears(doctorRequestDto.experienceYears());
        doctor.setActive(doctorRequestDto.active() != null ? doctorRequestDto.active() : true);
        doctor.setClinic(clinic);

        return mapToDoctorDto(doctorRepository.save(doctor));

    }

    @Override
    @Transactional
    public DoctorDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        Clinic clinic = clinicRepository.findById(doctorRequestDto.clinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic", "id", doctorRequestDto.clinicId()));

        doctor.setFullName(doctorRequestDto.fullName());
        doctor.setEmail(doctorRequestDto.email());
        doctor.setPhone(doctorRequestDto.phone());
        doctor.setTitle(doctorRequestDto.title());
        doctor.setSpecialty(doctorRequestDto.specialty());
        doctor.setBio(doctorRequestDto.bio());
        doctor.setImageUrl(doctorRequestDto.imageUrl());
        doctor.setExperienceYears(doctorRequestDto.experienceYears());
        doctor.setActive(doctorRequestDto.active() != null ? doctorRequestDto.active() : true);
        doctor.setClinic(clinic);

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return mapToDoctorDto(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        doctorRepository.delete(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(Long id) {

        Doctor doctor = doctorRepository.findByIdWithClinic(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        return mapToDoctorDto(doctor);
    }


    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getDoctorsByClinicId(Long clinicId) {
        return doctorRepository.findByClinicIdWithClinic(clinicId)
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialtyWithClinic(specialty)
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getActiveDoctors() {
        return doctorRepository.findActiveDoctorsWithClinic()
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getActiveDoctorsByClinicId(Long clinicId) {
        return doctorRepository.findActiveDoctorsByClinicIdWithClinic(clinicId)
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getMyClinicDoctors(
            String clinicAdminEmail
    ) {

        Clinic clinic =
                getAssignedClinicForClinicAdmin(
                        clinicAdminEmail
                );

        return doctorRepository
                .findByClinicIdWithClinic(
                        clinic.getId()
                )
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional
    public DoctorDto updateMyClinicDoctorActiveStatus(
            Long doctorId,
            Boolean active,
            String clinicAdminEmail
    ) {

        Clinic clinic =
                getAssignedClinicForClinicAdmin(
                        clinicAdminEmail
                );

        Doctor doctor =
                doctorRepository
                        .findByIdAndClinicIdWithClinic(
                                doctorId,
                                clinic.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor",
                                        "id",
                                        doctorId
                                )
                        );

        doctor.setActive(active);

        return mapToDoctorDto(doctor);
    }

    private DoctorDto mapToDoctorDto(Doctor doctor) {
        return new DoctorDto(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getTitle(),
                doctor.getSpecialty(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.getBio(),
                doctor.getImageUrl(),
                doctor.getExperienceYears(),
                doctor.getActive(),
                doctor.getClinic().getId(),
                doctor.getClinic().getName(),
                doctor.getCreatedAt()
        );
    }

    private Clinic getAssignedClinicForClinicAdmin(
            String clinicAdminEmail
    ) {

        String normalizedEmail =
                clinicAdminEmail.trim().toLowerCase();

        UserAccount userAccount =
                userAccountRepository
                        .findByEmail(normalizedEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User",
                                        "email",
                                        normalizedEmail
                                )
                        );

        if (userAccount.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalStateException(
                    "User must have CLINIC_ADMIN role"
            );
        }

        Clinic clinic = userAccount.getClinic();

        if (clinic == null) {
            throw new IllegalStateException(
                    "Clinic admin is not assigned to a clinic"
            );
        }

        return clinic;
    }
}
