package com.canaydin.mediconnect.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "mediconnect.cors")
public record CorsProperties(

        List<String> allowedOrigins,

        List<String> allowedMethods,

        List<String> allowedHeaders,

        boolean allowCredentials,

        Duration maxAge

) {
}