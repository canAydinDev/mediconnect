package com.canaydin.mediconnect.patient.repository;

import com.canaydin.mediconnect.patient.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientProfileRepository
        extends JpaRepository<PatientProfile, Long> {

    Optional<PatientProfile> findByUserAccountId(Long userId);

    Optional<PatientProfile> findByUserAccountEmail(String email);

    @Query("""
            select distinct p
            from PatientProfile p
            left join fetch p.favoriteDoctors d
            left join fetch d.clinic
            where p.userAccount.email = :email
            """)
    Optional<PatientProfile> findByUserAccountEmailWithFavoriteDoctors(
            @Param("email") String email
    );
}