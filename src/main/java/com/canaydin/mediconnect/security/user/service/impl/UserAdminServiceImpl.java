package com.canaydin.mediconnect.security.user.service.impl;

import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.clinic.enums.ClinicStatus;
import com.canaydin.mediconnect.clinic.repository.ClinicRepository;
import com.canaydin.mediconnect.exception.ResourceNotFoundException;
import com.canaydin.mediconnect.security.user.dto.UserAdminDto;
import com.canaydin.mediconnect.security.user.entity.UserAccount;
import com.canaydin.mediconnect.security.user.enums.Role;
import com.canaydin.mediconnect.security.user.repository.UserAccountRepository;
import com.canaydin.mediconnect.security.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserAccountRepository userAccountRepository;
    private final ClinicRepository clinicRepository;

    @Override
    @Transactional(readOnly = true)
    public UserAdminDto searchUserByEmail(
            String email
    ) {

        String normalizedEmail =
                email.trim().toLowerCase();

        UserAccount userAccount =
                userAccountRepository
                        .findByEmail(normalizedEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User",
                                        "email",
                                        email
                                )
                        );

        return mapToUserAdminDto(userAccount);
    }


    @Override
    @Transactional
    public UserAdminDto elevateToClinicAdmin(
            Long userId
    ) {

        UserAccount userAccount =
                userAccountRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User",
                                        "id",
                                        userId
                                )
                        );

        if (userAccount.getRole() == Role.CLINIC_ADMIN) {
            return mapToUserAdminDto(userAccount);
        }

        if (userAccount.getRole() == Role.ADMIN) {
            throw new IllegalStateException(
                    "ADMIN user cannot be elevated to CLINIC_ADMIN"
            );
        }

        if (userAccount.getRole() != Role.PATIENT) {
            throw new IllegalStateException(
                    "Only PATIENT users can be elevated to CLINIC_ADMIN"
            );
        }

        userAccount.setRole(Role.CLINIC_ADMIN);

        return mapToUserAdminDto(userAccount);
    }

    @Override
    @Transactional
    public UserAdminDto assignClinicToClinicAdmin(
            Long userId,
            Long clinicId
    ) {

        UserAccount userAccount =
                userAccountRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User",
                                        "id",
                                        userId
                                )
                        );

        if (userAccount.getRole() != Role.CLINIC_ADMIN) {
            throw new IllegalStateException(
                    "User must be CLINIC_ADMIN before clinic assignment"
            );
        }

        Clinic clinic =
                clinicRepository
                        .findById(clinicId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Clinic",
                                        "id",
                                        clinicId
                                )
                        );

        if (clinic.getStatus() != ClinicStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Inactive clinic cannot be assigned to a clinic admin"
            );
        }

        userAccount.setClinic(clinic);

        return mapToUserAdminDto(userAccount);
    }

    private UserAdminDto mapToUserAdminDto(
            UserAccount userAccount
    ) {

        Clinic clinic = userAccount.getClinic();

        return new UserAdminDto(
                userAccount.getId(),
                userAccount.getFullName(),
                userAccount.getEmail(),
                userAccount.getRole(),
                userAccount.isActive(),
                clinic != null ? clinic.getId() : null,
                clinic != null ? clinic.getName() : null
        );
    }
}
