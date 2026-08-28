package com.canaydin.mediconnect.config;


import com.canaydin.mediconnect.config.properties.CacheProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
@RequiredArgsConstructor
public class CaffeineCacheConfig {

    private final CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() {

        CaffeineCache clinicByIdCache =
                buildCache(
                        "clinicById",
                        cacheProperties.clinicById().ttl(),
                        cacheProperties.clinicById().maxSize()
                );

        CaffeineCache clinicListCache =
                buildCache(
                        "clinicList",
                        cacheProperties.clinicList().ttl(),
                        cacheProperties.clinicList().maxSize()
                );

        SimpleCacheManager cacheManager =
                new SimpleCacheManager();

        cacheManager.setCaches(
                List.of(
                        clinicByIdCache,
                        clinicListCache
                )
        );

        return cacheManager;
    }

    private CaffeineCache buildCache(
            String cacheName,
            Duration ttl,
            long maximumSize
    ) {

        return new CaffeineCache(
                cacheName,
                Caffeine.newBuilder()
                        .expireAfterWrite(ttl)
                        .maximumSize(maximumSize)
                        .build()
        );
    }
}