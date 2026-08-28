package com.canaydin.mediconnect.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "mediconnect.cache")
public record CacheProperties(

        CacheSpec clinicById,

        CacheSpec clinicList

) {

    public record CacheSpec(
            Duration ttl,
            long maxSize
    ) {
    }
}