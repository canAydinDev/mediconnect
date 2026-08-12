package com.canaydin.mediconnect.security.user.controller;

import com.canaydin.mediconnect.security.user.dto.UserAdminDto;
import com.canaydin.mediconnect.security.user.service.UserAdminService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
public class UserAdminController {

    private final UserAdminService userAdminService;


    @GetMapping(value = "/admin/search", version = "1.0")
    public ResponseEntity<UserAdminDto> searchUserByEmail(

            @RequestParam
            @NotBlank(message = "Email cannot be blank")
            @Email(message = "Invalid email format")
            String email
    ) {

        return ResponseEntity.ok(
                userAdminService.searchUserByEmail(email)
        );
    }


    @PatchMapping(
            value = "/admin/{userId}/elevate-to-clinic-admin",
            version = "1.0"
    )
    public ResponseEntity<UserAdminDto> elevateToClinicAdmin(

            @PathVariable
            @Positive(message = "User id must be greater than 0")
            Long userId
    ) {

        return ResponseEntity.ok(
                userAdminService.elevateToClinicAdmin(userId)
        );
    }


    @PatchMapping(
            value = "/admin/{userId}/clinic/{clinicId}",
            version = "1.0"
    )
    public ResponseEntity<UserAdminDto> assignClinicToClinicAdmin(

            @PathVariable
            @Positive(message = "User id must be greater than 0")
            Long userId,

            @PathVariable
            @Positive(message = "Clinic id must be greater than 0")
            Long clinicId
    ) {

        return ResponseEntity.ok(
                userAdminService.assignClinicToClinicAdmin(
                        userId,
                        clinicId
                )
        );
    }
}