package com.canaydin.mediconnect.security.user.service;

import com.canaydin.mediconnect.security.user.dto.UserAdminDto;

public interface UserAdminService {

    UserAdminDto searchUserByEmail(
            String email
    );

    UserAdminDto elevateToClinicAdmin(
            Long userId
    );

    UserAdminDto assignClinicToClinicAdmin(
            Long userId,
            Long clinicId
    );
}