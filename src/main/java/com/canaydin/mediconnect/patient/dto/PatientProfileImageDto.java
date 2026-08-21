package com.canaydin.mediconnect.patient.dto;

public record PatientProfileImageDto(

        byte[] content,
        String contentType,
        String fileName
) {
}
