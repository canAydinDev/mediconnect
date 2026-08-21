package com.canaydin.mediconnect.patient.service.impl;

import com.canaydin.mediconnect.clinic.enums.ClinicStatus;

import com.canaydin.mediconnect.doctor.dto.DoctorDto;
import com.canaydin.mediconnect.doctor.entity.Doctor;
import com.canaydin.mediconnect.doctor.repository.DoctorRepository;
import com.canaydin.mediconnect.exception.ResourceNotFoundException;
import com.canaydin.mediconnect.patient.entity.PatientProfile;
import com.canaydin.mediconnect.patient.repository.PatientProfileRepository;
import com.canaydin.mediconnect.patient.service.PatientFavoriteDoctorService;
import com.canaydin.mediconnect.security.user.entity.UserAccount;
import com.canaydin.mediconnect.security.user.enums.Role;
import com.canaydin.mediconnect.security.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientFavoriteDoctorServiceImpl
        implements PatientFavoriteDoctorService {

    private final PatientProfileRepository patientProfileRepository;
    private final UserAccountRepository userAccountRepository;
    private final DoctorRepository doctorRepository;


    @Override
    @Transactional
    public DoctorDto saveFavoriteDoctor(
            Long doctorId,
            String patientEmail
    ) {

        UserAccount patient =
                getPatientByEmail(patientEmail);

        PatientProfile profile =
                getOrCreatePatientProfile(patient);

        Doctor doctor =
                doctorRepository
                        .findActiveDoctorByIdWithClinic(doctorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor",
                                        "id",
                                        doctorId
                                )
                        );

        if (doctor.getClinic().getStatus()
                != ClinicStatus.ACTIVE) {

            throw new ResourceNotFoundException(
                    "Doctor",
                    "id",
                    doctorId
            );
        }

        boolean alreadyFavorite =
                profile.getFavoriteDoctors()
                        .stream()
                        .anyMatch(savedDoctor ->
                                savedDoctor
                                        .getId()
                                        .equals(doctorId)
                        );

        if (!alreadyFavorite) {
            profile.getFavoriteDoctors()
                    .add(doctor);
        }

        return mapToDoctorDto(doctor);
    }


    @Override
    @Transactional
    public void unsaveFavoriteDoctor(
            Long doctorId,
            String patientEmail
    ) {

        UserAccount patient =
                getPatientByEmail(patientEmail);

        PatientProfile profile =
                patientProfileRepository
                        .findByUserAccountId(
                                patient.getId()
                        )
                        .orElse(null);

        if (profile == null) {
            return;
        }

        profile.getFavoriteDoctors()
                .removeIf(doctor ->
                        doctor.getId().equals(doctorId)
                );
    }


    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getMyFavoriteDoctors(
            String patientEmail
    ) {

        String normalizedEmail =
                patientEmail.trim().toLowerCase();

        PatientProfile profile =
                patientProfileRepository
                        .findByUserAccountEmailWithFavoriteDoctors(
                                normalizedEmail
                        )
                        .orElse(null);

        if (profile == null) {
            return List.of();
        }

        return profile.getFavoriteDoctors()
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }


    private UserAccount getPatientByEmail(
            String patientEmail
    ) {

        String normalizedEmail =
                patientEmail.trim().toLowerCase();

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

        if (userAccount.getRole() != Role.PATIENT) {
            throw new IllegalStateException(
                    "User must have PATIENT role"
            );
        }

        return userAccount;
    }


    private PatientProfile getOrCreatePatientProfile(
            UserAccount patient
    ) {

        return patientProfileRepository
                .findByUserAccountId(patient.getId())
                .orElseGet(() -> {

                    PatientProfile profile =
                            new PatientProfile();

                    profile.setUserAccount(patient);

                    return patientProfileRepository
                            .save(profile);
                });
    }


    private DoctorDto mapToDoctorDto(
            Doctor doctor
    ) {

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
}