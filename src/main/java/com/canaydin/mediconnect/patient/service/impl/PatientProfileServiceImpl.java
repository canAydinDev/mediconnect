package com.canaydin.mediconnect.patient.service.impl;

import com.canaydin.mediconnect.exception.ResourceNotFoundException;
import com.canaydin.mediconnect.patient.dto.PatientProfileDto;
import com.canaydin.mediconnect.patient.dto.PatientProfileImageDto;
import com.canaydin.mediconnect.patient.dto.PatientProfileRequestDto;
import com.canaydin.mediconnect.patient.entity.PatientProfile;
import com.canaydin.mediconnect.patient.repository.PatientProfileRepository;
import com.canaydin.mediconnect.patient.service.PatientProfileService;
import com.canaydin.mediconnect.security.user.entity.UserAccount;
import com.canaydin.mediconnect.security.user.enums.Role;
import com.canaydin.mediconnect.security.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientProfileServiceImpl
        implements PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;
    private final UserAccountRepository userAccountRepository;


    @Override
    @Transactional
    public PatientProfileDto createOrUpdateMyProfile(
            PatientProfileRequestDto request,
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

        PatientProfile profile =
                patientProfileRepository
                        .findByUserAccountId(
                                userAccount.getId()
                        )
                        .orElseGet(() -> {
                            PatientProfile newProfile =
                                    new PatientProfile();

                            newProfile.setUserAccount(
                                    userAccount
                            );

                            return newProfile;
                        });

        profile.setPhone(request.phone());
        profile.setDateOfBirth(request.dateOfBirth());
        profile.setCity(request.city());
        profile.setAddress(request.address());

        PatientProfile savedProfile =
                patientProfileRepository.save(profile);

        return mapToDto(savedProfile);
    }

    @Override
    @Transactional
    public PatientProfileDto uploadMyProfileImage(
            MultipartFile image,
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

        PatientProfile profile =
                patientProfileRepository
                        .findByUserAccountId(
                                userAccount.getId()
                        )
                        .orElseGet(() -> {
                            PatientProfile newProfile =
                                    new PatientProfile();

                            newProfile.setUserAccount(
                                    userAccount
                            );

                            return newProfile;
                        });

        validateProfileImage(image);

        try {

            profile.setProfileImage(
                    image.getBytes()
            );

            profile.setProfileImageName(
                    image.getOriginalFilename()
            );

            profile.setProfileImageType(
                    image.getContentType()
            );

        } catch (IOException exception) {

            throw new UncheckedIOException(
                    "Could not read profile image",
                    exception
            );
        }

        PatientProfile savedProfile =
                patientProfileRepository.save(profile);

        return mapToDto(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientProfileDto getMyProfile(
            String patientEmail
    ) {

        String normalizedEmail =
                patientEmail.trim().toLowerCase();

        PatientProfile profile =
                patientProfileRepository
                        .findByUserAccountEmail(normalizedEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "PatientProfile",
                                        "email",
                                        normalizedEmail
                                )
                        );

        return mapToDto(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientProfileImageDto getMyProfileImage(
            String patientEmail
    ) {

        String normalizedEmail =
                patientEmail.trim().toLowerCase();

        PatientProfile profile =
                patientProfileRepository
                        .findByUserAccountEmail(normalizedEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "PatientProfile",
                                        "email",
                                        normalizedEmail
                                )
                        );

        byte[] image = profile.getProfileImage();

        if (image == null || image.length == 0) {
            throw new ResourceNotFoundException(
                    "ProfileImage",
                    "email",
                    normalizedEmail
            );
        }

        return new PatientProfileImageDto(
                image,
                profile.getProfileImageType(),
                profile.getProfileImageName()
        );
    }

    private void validateProfileImage(
            MultipartFile image
    ) {

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException(
                    "Profile image cannot be empty"
            );
        }

        String contentType = image.getContentType();

        if (contentType == null ||
                !List.of(
                        "image/jpeg",
                        "image/png",
                        "image/webp"
                ).contains(contentType)) {

            throw new IllegalArgumentException(
                    "Only JPEG, PNG and WEBP images are allowed"
            );
        }

        long maxSize = 5 * 1024 * 1024;

        if (image.getSize() > maxSize) {
            throw new IllegalArgumentException(
                    "Profile image cannot exceed 5 MB"
            );
        }
    }

    private PatientProfileDto mapToDto(
            PatientProfile profile
    ) {

        UserAccount user =
                profile.getUserAccount();

        return new PatientProfileDto(
                profile.getId(),

                user.getFullName(),
                user.getEmail(),

                profile.getPhone(),
                profile.getDateOfBirth(),
                profile.getCity(),
                profile.getAddress(),

                profile.getProfileImageName(),
                profile.getProfileImageType(),

                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
