package com.canaydin.mediconnect.doctor.dto;

import jakarta.validation.constraints.NotNull;

public record DoctorActiveStatusRequest(

        @NotNull(message = "Active status cannot be null")
        Boolean active

) {
}