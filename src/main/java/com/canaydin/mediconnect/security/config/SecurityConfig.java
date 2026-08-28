package com.canaydin.mediconnect.security.config;

import com.canaydin.mediconnect.config.properties.CorsProperties;

import com.canaydin.mediconnect.security.jwt.JwtAuthenticationFilter;
import com.canaydin.mediconnect.security.user.service.UserAccountDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsProperties corsProperties;
    private final UserAccountDetailsService userAccountDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    // =====================================================
    // AUTHENTICATION PROVIDER
    // =====================================================

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(
                        userAccountDetailsService
                );

        authenticationProvider.setPasswordEncoder(
                passwordEncoder
        );

        return authenticationProvider;
    }


    // =====================================================
    // AUTHENTICATION MANAGER
    // =====================================================

    @Bean
    public AuthenticationManager authenticationManager() {

        return new ProviderManager(
                authenticationProvider()
        );
    }


    // =====================================================
    // CORS
    // =====================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                corsProperties.allowedOrigins()
        );

        configuration.setAllowedMethods(
                corsProperties.allowedMethods()
        );

        configuration.setAllowedHeaders(
                corsProperties.allowedHeaders()
        );

        configuration.setAllowCredentials(
                corsProperties.allowCredentials()
        );

        configuration.setMaxAge(
                corsProperties.maxAge().getSeconds()
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }


    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http

                // =========================
                // CORS
                // =========================

                .cors(cors -> cors
                        .configurationSource(
                                corsConfigurationSource()
                        )
                )


                // =========================
                // STATELESS API
                // =========================

                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // =========================
                // EXCEPTION HANDLING
                // =========================

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.getWriter().write("""
                                            {
                                              "error": "Unauthorized",
                                              "message": "Authentication is required to access this resource"
                                            }
                                            """);
                                }
                        )

                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.getWriter().write("""
                                            {
                                              "error": "AccessDenied",
                                              "message": "You do not have permission to access this resource"
                                            }
                                            """);
                                }
                        )
                )


                // =========================
                // AUTHORIZATION
                // =========================

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC
                        // =========================

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/swagger-ui.html",
                                "/api/swagger-ui/**",
                                "/api/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/contact-messages"
                        ).permitAll()


                        // =========================
                        // PATIENT
                        // =========================

                        .requestMatchers(
                                "/api/patients/me",
                                "/api/patients/me/**"
                        ).hasRole("PATIENT")


                        // =========================
                        // CLINIC ADMIN
                        // =========================

                        .requestMatchers(
                                "/api/doctors/clinic-admin",
                                "/api/doctors/clinic-admin/**"
                        ).hasRole("CLINIC_ADMIN")


                        // =========================
                        // ADMIN
                        // =========================

                        .requestMatchers(
                                "/api/contact-messages/admin",
                                "/api/contact-messages/admin/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/clinics/admin",
                                "/api/clinics/admin/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/users/admin",
                                "/api/users/admin/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/doctors"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/doctors/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/doctors/*/active"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/doctors/*"
                        ).hasRole("ADMIN")


                        // =========================
                        // OTHER AUTHENTICATED APIs
                        // =========================

                        .anyRequest()
                        .authenticated()
                )


                // =========================
                // AUTHENTICATION
                // =========================

                .authenticationProvider(
                        authenticationProvider()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}