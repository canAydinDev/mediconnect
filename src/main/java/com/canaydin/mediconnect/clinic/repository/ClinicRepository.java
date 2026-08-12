package com.canaydin.mediconnect.clinic.repository;

import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.clinic.enums.ClinicStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository
        extends JpaRepository<Clinic, Long> {

    Page<Clinic> findByStatus(
            ClinicStatus status,
            Pageable pageable
    );

    Optional<Clinic> findByIdAndStatus(
            Long id,
            ClinicStatus status
    );
}
