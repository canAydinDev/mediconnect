package com.canaydin.mediconnect.security.config;

import com.github.benmanes.caffeine.cache.Caffeine;
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
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {

        CaffeineCache clinicByIdCache =
                new CaffeineCache(
                        "clinicById",
                        Caffeine.newBuilder()
                                .expireAfterWrite(Duration.ofMinutes(10))
                                .maximumSize(500)
                                .build()
                );

        CaffeineCache clinicListCache =
                new CaffeineCache(
                        "clinicList",
                        Caffeine.newBuilder()
                                .expireAfterWrite(Duration.ofMinutes(5))
                                .maximumSize(200)
                                .build()
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
}