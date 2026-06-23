package com.canaydin.mediconnect.exception;

import java.time.Instant;

public record ErrorResponse(
        String apiPath,
        String errorCode,
        String errorMessage,
        Instant errorTime
) {
}
