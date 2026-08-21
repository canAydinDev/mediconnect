package com.canaydin.mediconnect.patient.repository;

import com.canaydin.mediconnect.patient.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientProfileRepository
        extends JpaRepository<PatientProfile, Long> {

    Optional<PatientProfile> findByUserAccountId(Long userId);

    Optional<PatientProfile> findByUserAccountEmail(String email);
}