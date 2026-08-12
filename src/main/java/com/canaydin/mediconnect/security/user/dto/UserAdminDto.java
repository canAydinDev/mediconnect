package com.canaydin.mediconnect.security.user.dto;

import com.canaydin.mediconnect.security.user.enums.Role;

public record UserAdminDto(
        Long id,
        String fullName,
        String email,
        Role role,
        boolean active,
        Long clinicId,
        String clinicName
) {
}
